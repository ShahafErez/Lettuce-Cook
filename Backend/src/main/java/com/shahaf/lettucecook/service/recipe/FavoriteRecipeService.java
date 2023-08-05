package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.FavoriteRecipeDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Favorite;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.exceptions.ResourceAlreadyExistsException;
import com.shahaf.lettucecook.reposetory.recipe.FavoriteRecipeRepository;
import com.shahaf.lettucecook.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteRecipeService {
    @Autowired
    private FavoriteRecipeRepository favoriteRecipeRepository;
    @Autowired
    private RecipeGlobalService recipeGlobalService;
    @Autowired
    private UserService userService;

    public void addFavoriteRecipe(FavoriteRecipeDto favoriteRecipeDto) {
        User user = userService.getUserByUsername(favoriteRecipeDto.getUsername());
        Recipe recipe = recipeGlobalService.getRecipeById(Long.valueOf(favoriteRecipeDto.getRecipeId()));
        validateRecipeNotAddedAsFavorite(recipe.getId(), user.getId());
        createNewFavoriteObject(user, recipe);
    }

    private void validateRecipeNotAddedAsFavorite(Long recipeId, Long userId) {
        if (favoriteRecipeRepository.findByRecipeIdAndUserId(recipeId, userId).isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("Recipe %s already saved as favorite by user %s", recipeId, userId));
        }
    }

    private void createNewFavoriteObject(User user, Recipe recipe) {
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        favoriteRecipeRepository.save(favorite);
    }

    @Transactional
    public void deleteAllFavoritesByRecipe(Long recipeId) {
        favoriteRecipeRepository.deleteAllFavoritesByRecipe(recipeId);
    }

    @Transactional
    public void removeFavoriteRecipe(Long recipeId, String username) {
        User user = userService.getUserByUsername(username);
        favoriteRecipeRepository.deleteByRecipeIdAndUserId(recipeId, user.getId());
    }
}
