package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Favorite;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.exceptions.ResourceAlreadyExistsException;
import com.shahaf.lettucecook.reposetory.recipe.FavoriteRecipeRepository;
import com.shahaf.lettucecook.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private GlobalRecipeService globalRecipeService;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(FavoriteRecipeService.class);

    public List<Recipe> getFavoritesByUser() {
        User user = userService.getUserFromToken();

        logger.info("Fetching favorite recipes for user {}.", user.getActualUsername());
        Optional<List<Favorite>> favoritesList = favoriteRecipeRepository.getFavoritesByUser(user.getId());
        return favoritesList.map(favorites -> favorites.stream().map(Favorite::getRecipe).collect(Collectors.toList())).orElse(null);
    }

    public void addFavoriteRecipe(Long recipeId) {
        User user = userService.getUserFromToken();
        Recipe recipe = globalRecipeService.getRecipeById(recipeId);
        validateRecipeNotAddedAsFavorite(recipeId, user.getId());

        logger.info("Adding recipe {} as favorite to user {}.", recipe.getId(), user.getActualUsername());
        Favorite favorite = new Favorite(recipe, user);
        favoriteRecipeRepository.save(favorite);
    }

    private void validateRecipeNotAddedAsFavorite(Long recipeId, Long userId) {
        if (favoriteRecipeRepository.findByRecipeIdAndUserId(recipeId, userId).isPresent()) {
            String errorMessage = String.format("Recipe %d already saved as favorite by user ID %d", recipeId, userId);
            logger.error(errorMessage);
            throw new ResourceAlreadyExistsException(errorMessage);
        }
    }

    @Transactional
    public void deleteAllFavoritesByRecipe(Long recipeId) {
        logger.info("Removing all favorites for recipe {}.", recipeId);
        favoriteRecipeRepository.deleteAllFavoritesByRecipe(recipeId);
    }

    @Transactional
    public void removeFavoriteRecipe(Long recipeId) {
        User user = userService.getUserFromToken();
        globalRecipeService.validateRecipeExists(recipeId);
        globalRecipeService.validateRecipeIsFavoriteByUser(recipeId, user);

        logger.info("Removing recipe {} from favorites for user {}.", recipeId, user.getActualUsername());
        favoriteRecipeRepository.deleteByRecipeIdAndUserId(recipeId, user.getId());

    }

    public void deleteAllFavorites() {
        logger.info("Removing all favorites from DB");
        favoriteRecipeRepository.deleteAll();
    }
}
