package com.shahaf.lettucecook.service;

import com.shahaf.lettucecook.dto.RecipeFavoriteDto;
import com.shahaf.lettucecook.entity.Favorite;
import com.shahaf.lettucecook.entity.Recipe;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.exceptions.ResourceAlreadyExistsException;
import com.shahaf.lettucecook.reposetory.RecipesFavoriteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeFavoriteService {
    @Autowired
    private RecipesFavoriteRepository recipesFavoriteRepository;
    @Autowired
    private RecipeGlobalService recipeGlobalService;
    @Autowired
    private UserService userService;

    public void addRecipeFavorite(RecipeFavoriteDto recipeFavoriteDto) {
        User user = userService.getUserByUsername(recipeFavoriteDto.getUsername());
        Recipe recipe = recipeGlobalService.getRecipeById(Long.valueOf(recipeFavoriteDto.getRecipeId()));
        validateRecipeNotAddedAsFavorite(recipe.getId(), user.getId());
        createNewFavoriteObject(user, recipe);
    }

    private void validateRecipeNotAddedAsFavorite(Long recipeId, Integer userId) {
        if (recipesFavoriteRepository.findByRecipeIdAndUserId(recipeId, userId).isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("Recipe %s already saved as favorite by user %s", recipeId, userId));
        }
    }

    private void createNewFavoriteObject(User user, Recipe recipe) {
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        recipesFavoriteRepository.save(favorite);
    }

    @Transactional
    public void deleteAllFavoritesByRecipe(Long recipeId) {
        recipesFavoriteRepository.deleteAllFavoritesByRecipe(recipeId);
    }
}
