package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.exceptions.ResourceNotFound;
import com.shahaf.lettucecook.reposetory.recipe.FavoriteRecipeRepository;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobalRecipeService {

    @Autowired
    RecipesRepository recipesRepository;

    @Autowired
    FavoriteRecipeRepository favoriteRecipeRepository;

    public Recipe getRecipeById(Long id) {
        return recipesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Recipe not found with id: " + id));
    }

    public RecipeUserDto mapRecipeToRecipeUserDto(Recipe recipe, User user) {
        boolean isFavorite = (user != null) && isRecipeFavoriteByUser(recipe.getId(), user);
        return new RecipeUserDto(recipe, isFavorite);
    }

    public boolean isRecipeFavoriteByUser(Long recipeId, User user) {
        return favoriteRecipeRepository.findByRecipeIdAndUserId(recipeId, user.getId()).isPresent();
    }
}
