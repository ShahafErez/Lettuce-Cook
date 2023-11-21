package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.mapper.RecipeElasticMapper;
import com.shahaf.lettucecook.mapper.RecipeMapper;
import com.shahaf.lettucecook.reposetory.elasticsearch.RecipeElasticRepository;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import com.shahaf.lettucecook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private RecipesRepository recipesRepository;
    @Autowired
    private RecipeElasticRepository recipeElasticRepository;
    @Autowired
    private RecipeGlobalService recipeGlobalService;
    @Autowired
    private FavoriteRecipeService favoriteRecipeService;
    @Autowired
    private UserService userService;
    @Autowired
    private RecipeMapper recipeMapper;
    @Autowired
    private RecipeElasticMapper recipeElasticMapper;

    public List<RecipeUserDto> getRecipes(Integer numOfRecipes, Category category, Boolean random) {
        User user = userService.getUserFromToken();

        if (category != null) {
            return getRecipesByCategory(category, numOfRecipes, random, user);
        }

        return getRecipes(numOfRecipes, random, user);

    }

    private List<RecipeUserDto> getRecipes(Integer numOfRecipes, Boolean random, User user) {
        List<RecipeUserDto> recipes;
        // if recipes should be randomize-getting all recipes, and then shuffling
        if (Boolean.TRUE.equals(random)) {
            recipes = getAllRecipes(user);
            Collections.shuffle(recipes);
            if (numOfRecipes != null) {
                recipes = recipes.subList(0, numOfRecipes);
            }
        } else {
            if (numOfRecipes != null) {
                recipes = getNumberOfRecipes(numOfRecipes, user);
            } else {
                recipes = getAllRecipes(user);
            }
        }
        return recipes;
    }

    private List<RecipeUserDto> getRecipesByCategory(Category category, Integer numOfRecipes, Boolean random, User user) {
        List<RecipeUserDto> recipesByCategory;
        // if recipes should be randomize-getting all recipes by category, and then shuffling
        if (Boolean.TRUE.equals(random)) {
            recipesByCategory = getAllRecipesByCategory(user, category);
            Collections.shuffle(recipesByCategory);
            if (numOfRecipes != null) {
                recipesByCategory = recipesByCategory.subList(0, numOfRecipes);
            }
        } else {
            if (numOfRecipes != null) {
                recipesByCategory = getNumberOfRecipesByCategory(category, numOfRecipes, user);
            } else {
                recipesByCategory = getAllRecipesByCategory(user, category);
            }
        }
        return recipesByCategory;
    }

    private List<RecipeUserDto> getAllRecipes(User user) {
        return recipesRepository.findRecipesByOrderByIdDesc().stream().map(recipe -> mapRecipeToRecipeUserDto(recipe, user)).collect(Collectors.toList());

    }

    private List<RecipeUserDto> getAllRecipesByCategory(User user, Category category) {
        Optional<List<Recipe>> queryResult = recipesRepository.getRecipesByCategory(category);
        return queryResult.map(recipes -> recipes.stream().map(recipe -> mapRecipeToRecipeUserDto(recipe, user)).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    private List<RecipeUserDto> getNumberOfRecipes(Integer numOfRecipes, User user) {
        Pageable pageable = PageRequest.of(0, numOfRecipes);
        List<Recipe> queryResult = recipesRepository.findNRecipesByOrderByIdDesc(pageable);
        return queryResult.stream().map(recipe -> mapRecipeToRecipeUserDto(recipe, user)).collect(Collectors.toList());
    }

    private List<RecipeUserDto> getNumberOfRecipesByCategory(Category category, Integer numOfRecipes, User user) {
        Pageable pageable = PageRequest.of(0, numOfRecipes);
        Optional<List<Recipe>> queryResult = recipesRepository.getNRecipesByCategory(category, pageable);
        return queryResult.map(recipes -> recipes.stream().map(recipe -> mapRecipeToRecipeUserDto(recipe, user)).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    private RecipeUserDto mapRecipeToRecipeUserDto(Recipe recipe, User user) {
        boolean isFavorite = (user != null) && favoriteRecipeService.isFavorite(recipe.getId(), user);
        return new RecipeUserDto(recipe, isFavorite);
    }

    public RecipeUserDto getRecipeById(Long recipeId) {
        User user = userService.getUserFromToken();
        Recipe recipe = recipeGlobalService.getRecipeById(recipeId);
        boolean isFavorite = (user != null) && favoriteRecipeService.isFavorite(recipeId, user);
        return new RecipeUserDto(recipe, isFavorite);
    }

    public Recipe addRecipe(RecipeCreationDto recipeCreationDto) throws IOException {
        Recipe recipeCreation = recipeMapper.recipeDtoToRecipe(recipeCreationDto);
        Recipe createdRecipe = recipesRepository.save(recipeCreation);
        recipeElasticRepository.save(recipeElasticMapper.recipeToElasticRecipe(createdRecipe));

        return createdRecipe;
    }

    public void deleteRecipe(Long recipeId) {
        Recipe recipe = recipeGlobalService.getRecipeById(recipeId);
        if (recipe != null) {
            favoriteRecipeService.deleteAllFavoritesByRecipe(recipeId);
            recipesRepository.deleteById(recipeId);
        }
    }

    public void deleteAllRecipes() {
        favoriteRecipeService.deleteAllFavorites();
        recipesRepository.deleteAll();
    }
}
