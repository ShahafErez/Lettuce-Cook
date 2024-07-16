package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.exceptions.ErrorOccurredException;
import com.shahaf.lettucecook.mapper.RecipeMapper;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import com.shahaf.lettucecook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    @Autowired
    RecipesRepository recipesRepository;
    @Autowired
    RecipeMapper recipeMapper;
    @Autowired
    GlobalRecipeService globalRecipeService;
    @Autowired
    FavoriteRecipeService favoriteRecipeService;
    @Autowired
    UserService userService;
    @Autowired
    ElasticService elasticService;
    private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);

    public List<RecipeUserDto> getRecipes(Integer numOfRecipes, Category category, Boolean random) {
        User user = userService.getUserFromToken();
        logger.info("Fetching recipes. Number of recipes: {}, category: {}, is randomize: {}", numOfRecipes, category, random);
        if (category != null) {
            return getRecipesByCategory(numOfRecipes, category, random, user);
        }
        return getRecipes(numOfRecipes, random, user);
    }

    private List<RecipeUserDto> getRecipes(Integer numOfRecipes, Boolean random, User user) {
        List<Recipe> recipes;

        // if recipes should be randomize-getting all recipes, and then shuffling
        if (Boolean.TRUE.equals(random)) {
            recipes = getAllRecipes();
            Collections.shuffle(recipes);
            if (numOfRecipes != null && (recipes.size() > numOfRecipes)) {
                recipes = recipes.subList(0, numOfRecipes);
            }
        } else {
            if (numOfRecipes != null) {
                recipes = getNumberOfRecipes(numOfRecipes);
            } else {
                recipes = getAllRecipes();
            }
        }

        if (recipes == null) {
            return Collections.emptyList();
        }
        return recipes.stream().map(recipe -> globalRecipeService.mapRecipeToRecipeUserDto(recipe, user)).collect(Collectors.toList());
    }

    private List<RecipeUserDto> getRecipesByCategory(Integer numOfRecipes, Category category, Boolean random, User user) {
        List<Recipe> recipesByCategory;

        // if recipes should be randomize-getting all recipes by category, and then shuffling
        if (Boolean.TRUE.equals(random)) {
            recipesByCategory = getAllRecipesByCategory(category);
            if (recipesByCategory == null) {
                return Collections.emptyList();
            }
            Collections.shuffle(recipesByCategory);
            if (numOfRecipes != null && recipesByCategory.size() > numOfRecipes) {
                recipesByCategory = recipesByCategory.subList(0, numOfRecipes);
            }
        } else {
            if (numOfRecipes != null) {
                recipesByCategory = getNumberOfRecipesByCategory(numOfRecipes, category);
            } else {
                recipesByCategory = getAllRecipesByCategory(category);
            }
        }

        if (recipesByCategory == null) {
            return Collections.emptyList();
        }
        return recipesByCategory.stream().map(recipe -> globalRecipeService.mapRecipeToRecipeUserDto(recipe, user)).collect(Collectors.toList());
    }

    private List<Recipe> getAllRecipes() {
        logger.info("Fetching all recipes by descending recipe ID order");
        return recipesRepository.findRecipesByOrderByIdDesc();
    }

    private List<Recipe> getAllRecipesByCategory(Category category) {
        logger.info("Fetching all recipes by category: {}.", category);
        Optional<List<Recipe>> queryResult = recipesRepository.getRecipesByCategory(category);
        return queryResult.orElse(null);
    }

    private List<Recipe> getNumberOfRecipes(Integer numOfRecipes) {
        Pageable pageable = PageRequest.of(0, numOfRecipes);
        logger.info("Fetching number of recipes: {}.", numOfRecipes);
        return recipesRepository.findNRecipesByOrderByIdDesc(pageable);
    }

    private List<Recipe> getNumberOfRecipesByCategory(Integer numOfRecipes, Category category) {
        Pageable pageable = PageRequest.of(0, numOfRecipes);
        logger.info("Fetching number of recipes: {} by category: {}.", numOfRecipes, category);
        Optional<List<Recipe>> queryResult = recipesRepository.getNRecipesByCategory(category, pageable);
        return queryResult.orElse(null);
    }

    public RecipeUserDto getRecipeById(Long recipeId) {
        User user = userService.getUserFromToken();
        logger.info("Fetching recipe by ID {}.", recipeId);
        Recipe recipe = globalRecipeService.getRecipeById(recipeId);
        boolean isFavorite = (user != null) && globalRecipeService.isRecipeFavoriteByUser(recipeId, user);
        return new RecipeUserDto(recipe, isFavorite);
    }

    public Recipe addRecipe(RecipeCreationDto recipeCreationDto) throws IOException {
        Long id = null;
        try {
            logger.info("Attempting to add a new recipe. Recipe name: {}", recipeCreationDto.getName());
            Recipe recipe = saveRecipe(recipeCreationDto);
            id = recipe.getId();
            elasticService.saveRecipe(recipe);
            logger.info("Recipe added successfully. Recipe ID: {}", id);
            return recipe;
        } catch (Exception e) {
            logger.error("Failed to add recipe. Recipe name {}.", recipeCreationDto.getName());
            if (id != null) {
                handleSaveRecipeRollback(id);
            }
            throw e;
        }
    }

    private Recipe saveRecipe(RecipeCreationDto recipeCreationDto) throws IOException {
        logger.info("Saving recipe to relational database. Recipe name: {}", recipeCreationDto.getName());
        Recipe recipeCreation = recipeMapper.recipeDtoToRecipe(recipeCreationDto);
        try {
            return recipesRepository.save(recipeCreation);
        } catch (Exception e) {
            String errorMessage = "Failed to add recipe to relational database. Recipe name: " + recipeCreationDto.getName();
            logger.error(errorMessage);
            throw new ErrorOccurredException(errorMessage);
        }
    }

    private void handleSaveRecipeRollback(Long recipeId) {
        logger.info("Rolling back and deleting recipe. Recipe ID: {}.", recipeId);
        recipesRepository.deleteById(recipeId);
    }

    public void deleteRecipe(Long recipeId) {
        Recipe recipe = globalRecipeService.getRecipeById(recipeId);
        if (recipe != null) {
            logger.info("Deleting recipe. Recipe ID: {}, Title: {}", recipeId, recipe.getName());
            favoriteRecipeService.deleteAllFavoritesByRecipe(recipeId);
            recipesRepository.deleteById(recipeId);
            elasticService.deleteRecipeById(recipeId);
        }
    }

    public void deleteAllRecipes() {
        logger.info("Deleting all recipes from all DB.");
        favoriteRecipeService.deleteAllFavorites();
        recipesRepository.deleteAll();
        elasticService.deleteAllRecipes();
    }
}
