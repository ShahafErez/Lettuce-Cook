package com.shahaf.recipe_service.service;


import com.shahaf.recipe_service.entity.Favorite;
import com.shahaf.recipe_service.entity.Recipe;
import com.shahaf.recipe_service.exceptions.ResourceAlreadyExistsException;
import com.shahaf.recipe_service.exceptions.ResourceNotFound;
import com.shahaf.recipe_service.repository.FavoriteRecipeRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteRecipeService {
    @Autowired
    private RecipeHelperService recipeHelperService;
    @Autowired
    private FavoriteRecipeRepository favoriteRecipeRepository;
    private static final Logger logger = LoggerFactory.getLogger(FavoriteRecipeService.class);

    public List<Recipe> getFavoritesByUser(String username) {
        logger.info("Fetching favorite recipes for user {}.", username);
        Optional<List<Favorite>> favoritesList = favoriteRecipeRepository.getFavoritesByUser(username);
        if (!favoritesList.isPresent() || favoritesList.get().isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> recipeIds = favoritesList.get().stream()
                .map(Favorite::getRecipeId)
                .collect(Collectors.toList());
        Collections.reverse(recipeIds);

        return recipeHelperService.getRecipesById(recipeIds);
    }

    public void addRecipeToFavorites(Long recipeId, String username) {
        recipeHelperService.validateRecipeExists(recipeId);
        validateRecipeNotAddedAsFavorite(recipeId, username);
        logger.info("Adding recipe {} as favorite to user {}.", recipeId, username);
        Favorite favorite = new Favorite(recipeId, username);
        favoriteRecipeRepository.save(favorite);
    }

    private void validateRecipeNotAddedAsFavorite(Long recipeId, String username) {
        if (favoriteRecipeRepository.findByRecipeIdAndUserId(recipeId, username).isPresent()) {
            String errorMessage = String.format("Recipe %d already saved as favorite by user %s", recipeId, username);
            logger.error(errorMessage);
            throw new ResourceAlreadyExistsException(errorMessage);
        }
    }

    @Transactional
    public void deleteAllFavoritesByRecipe(Long recipeId) {
        logger.info("Removing all favorites for recipe {}.", recipeId);
        favoriteRecipeRepository.deleteAllFavoritesByRecipe(recipeId);
    }

    public void deleteAllFavorites() {
        logger.info("Removing all favorites from DB");
        favoriteRecipeRepository.deleteAll();
    }

    @Transactional
    public void removeRecipeFromFavorites(Long recipeId, String username) {
        recipeHelperService.validateRecipeExists(recipeId);
        validateRecipeIsFavoriteByUser(recipeId, username);
        logger.info("Removing recipe {} from favorites for user {}.", recipeId, username);
        favoriteRecipeRepository.deleteByRecipeIdAndUserId(recipeId, username);
    }

    public void validateRecipeIsFavoriteByUser(Long recipeId, String username) {
        boolean isFavoriteByUser = isRecipeFavoriteByUser(recipeId, username);
        if (!isFavoriteByUser) {
            String errorMessage = String.format("Recipe %d was not added as favorite by user %s.", recipeId, username);
            logger.error(errorMessage);
            throw new ResourceNotFound(errorMessage);
        }
    }

    public boolean isRecipeFavoriteByUser(Long recipeId, String username) {
        return favoriteRecipeRepository.existsByRecipeIdAndUserId(recipeId, username);
    }
}
