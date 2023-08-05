package com.shahaf.lettucecook.service;

import com.shahaf.lettucecook.dto.RecipeFavoriteDto;
import com.shahaf.lettucecook.entity.Favorite;
import com.shahaf.lettucecook.entity.Recipe;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.exceptions.ResourceAlreadyExistsException;
import com.shahaf.lettucecook.reposetory.RecipesFavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeFavoriteService {
    @Autowired
    private RecipesFavoriteRepository recipesFavoriteRepository;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    public void addRecipeFavorite(RecipeFavoriteDto recipeFavoriteDto) {
        User user = userService.getUserByUsername(recipeFavoriteDto.getUsername());
        Recipe recipe = recipeService.getRecipeById(Long.valueOf(recipeFavoriteDto.getRecipeId()));
        validateRecipeNotAddedAsFavorite(recipe, user);
        createNewFavoriteObject(user, recipe);
    }

    private void validateRecipeNotAddedAsFavorite(Recipe recipe, User user) {
        if (recipesFavoriteRepository.findByRecipeIdAndUserId(recipe, user).isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("Recipe %s already saved as favorite by user %s", recipe.getId(), user.getId()));
        }
    }

    private void createNewFavoriteObject(User user, Recipe recipe) {
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        recipesFavoriteRepository.save(favorite);
    }
}
