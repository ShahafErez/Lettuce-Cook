package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.exceptions.ResourceNotFound;
import com.shahaf.lettucecook.reposetory.recipe.FavoriteRecipeRepository;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GlobalRecipeService {
    @Autowired
    RecipesRepository recipesRepository;
    @Autowired
    FavoriteRecipeRepository favoriteRecipeRepository;
    private static final Logger logger = LoggerFactory.getLogger(GlobalRecipeService.class);

    public Recipe getRecipeById(Long id) {
        logger.info("Fetching recipe {} from repository.", id);
        Optional<Recipe> recipe = recipesRepository.findById(id);
        if (!recipe.isPresent()) {
            String errorMessage = "Recipe not found. ID: " + id;
            logger.error(errorMessage);
            throw new ResourceNotFound(errorMessage);
        }
        return recipe.get();
    }

    public RecipeUserDto mapRecipeToRecipeUserDto(Recipe recipe, User user) {
        boolean isFavorite = (user != null) && isRecipeFavoriteByUser(recipe.getId(), user);
        return new RecipeUserDto(recipe, isFavorite);
    }

    public void validateRecipeIsFavoriteByUser(Long recipeId, User user) {
        if (!isRecipeFavoriteByUser(recipeId, user)) {
            String errorMessage = String.format("Recipe %d is not favorite by user %s.", recipeId, user.getActualUsername());
            logger.error(errorMessage);
            throw new ResourceNotFound(errorMessage);
        }
    }

    public boolean isRecipeFavoriteByUser(Long recipeId, User user) {
        logger.info("Checking if recipe {} is favorite by user {}.", recipeId, user.getActualUsername());
        return favoriteRecipeRepository.existsByRecipeIdAndUserId(recipeId, user.getId());

    }

    public void validateRecipeExists(Long recipeId) {
        logger.info("Checking if recipe {} exists in DB.", recipeId);
        if (!recipesRepository.existsById(recipeId)) {
            String errorMessage = String.format("Recipe %d was not found.", recipeId);
            logger.error(errorMessage);
            throw new ResourceNotFound(errorMessage);
        }
    }}
