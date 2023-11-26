package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.mapper.RecipeMapper;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import com.shahaf.lettucecook.service.UserService;
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
    @Autowired
    RedisService redisService;

    public List<RecipeUserDto> getRecipes(Integer numOfRecipes, Category category, Boolean random) {
        User user = userService.getUserFromToken();

        if (category != null) {
            return getRecipesByCategory(category, numOfRecipes, random, user);
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

    private List<RecipeUserDto> getRecipesByCategory(Category category, Integer numOfRecipes, Boolean random, User user) {
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
                recipesByCategory = getNumberOfRecipesByCategory(category, numOfRecipes);
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
        return recipesRepository.findRecipesByOrderByIdDesc();
    }

    private List<Recipe> getAllRecipesByCategory(Category category) {
        Optional<List<Recipe>> queryResult = recipesRepository.getRecipesByCategory(category);
        return queryResult.orElse(null);
    }

    private List<Recipe> getNumberOfRecipes(Integer numOfRecipes) {
        Pageable pageable = PageRequest.of(0, numOfRecipes);
        return recipesRepository.findNRecipesByOrderByIdDesc(pageable);
    }

    private List<Recipe> getNumberOfRecipesByCategory(Category category, Integer numOfRecipes) {
        Pageable pageable = PageRequest.of(0, numOfRecipes);
        Optional<List<Recipe>> queryResult = recipesRepository.getNRecipesByCategory(category, pageable);
        return queryResult.orElse(null);
    }

    public RecipeUserDto getRecipeById(Long recipeId) {
        User user = userService.getUserFromToken();
        Recipe recipe = globalRecipeService.getRecipeById(recipeId);
        boolean isFavorite = (user != null) && globalRecipeService.isRecipeFavoriteByUser(recipeId, user);
        return new RecipeUserDto(recipe, isFavorite);
    }

    public Recipe addRecipe(RecipeCreationDto recipeCreationDto) throws IOException {
        Recipe recipeCreation = recipeMapper.recipeDtoToRecipe(recipeCreationDto);
        Recipe recipe = recipesRepository.save(recipeCreation);
        elasticService.saveRecipe(recipe);
        redisService.saveRecipe(recipe);
        return recipe;
    }

    public void deleteRecipe(Long recipeId) {
        Recipe recipe = globalRecipeService.getRecipeById(recipeId);
        if (recipe != null) {
            favoriteRecipeService.deleteAllFavoritesByRecipe(recipeId);
            recipesRepository.deleteById(recipeId);
            elasticService.deleteRecipeById(recipeId.toString());
            redisService.deleteRecipeById(recipeId);
        }
    }

    public void deleteAllRecipes() {
        favoriteRecipeService.deleteAllFavorites();
        recipesRepository.deleteAll();
        elasticService.deleteAllRecipes();
        redisService.deleteAllRecipes();
    }
}
