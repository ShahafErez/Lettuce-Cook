package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.FavoriteRecipeDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Favorite;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.exceptions.ResourceAlreadyExistsException;
import com.shahaf.lettucecook.exceptions.ResourceNotFound;
import com.shahaf.lettucecook.reposetory.recipe.FavoriteRecipeRepository;
import com.shahaf.lettucecook.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteRecipeService {
    @Autowired
    private FavoriteRecipeRepository favoriteRecipeRepository;
    @Autowired
    private RecipeGlobalService recipeGlobalService;
    @Autowired
    private UserService userService;

    public List<Recipe> getFavoritesByUser(String username) {
        Long userId = userService.getUserByUsername(username).getId();
        Optional<List<Favorite>> favoritesList = favoriteRecipeRepository.getFavoritesByUser(userId);
        if (!(favoritesList.isPresent())) {
            throw new ResourceNotFound(String.format("User %s doesn't have favorite recipes", username));
        }
        return favoritesList.get().stream().map(Favorite::getRecipe).collect(Collectors.toList());
    }

    public void addFavoriteRecipe(FavoriteRecipeDto favoriteRecipeDto) {
        User user = userService.getUserByUsername(favoriteRecipeDto.getUsername());
        Recipe recipe = recipeGlobalService.getRecipeById(favoriteRecipeDto.getRecipeId());
        validateRecipeNotAddedAsFavorite(recipe.getId(), user.getId(), favoriteRecipeDto.getUsername());
        favoriteRecipeRepository.save(createNewFavoriteObject(user, recipe));
    }

    private void validateRecipeNotAddedAsFavorite(Long recipeId, Long userId, String username) {
        if (favoriteRecipeRepository.findByRecipeIdAndUserId(recipeId, userId).isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("Recipe %d already saved as favorite by user %s", recipeId, username));
        }
    }

    private Favorite createNewFavoriteObject(User user, Recipe recipe) {
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        return favorite;
    }

    @Transactional
    public void deleteAllFavoritesByRecipe(Long recipeId) {
        favoriteRecipeRepository.deleteAllFavoritesByRecipe(recipeId);
    }

    @Transactional
    public void removeFavoriteRecipe(FavoriteRecipeDto favoriteRecipeDto) {
        User user = userService.getUserByUsername(favoriteRecipeDto.getUsername());
        favoriteRecipeRepository.deleteByRecipeIdAndUserId(favoriteRecipeDto.getRecipeId(), user.getId());
    }

    public void deleteAllFavorites() {
        favoriteRecipeRepository.deleteAll();
    }
}
