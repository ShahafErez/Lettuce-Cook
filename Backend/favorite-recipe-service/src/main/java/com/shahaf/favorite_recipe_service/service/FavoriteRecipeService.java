package com.shahaf.favorite_recipe_service.service;


import com.shahaf.favorite_recipe_service.entity.Favorite;
import com.shahaf.favorite_recipe_service.entity.Recipe;
import com.shahaf.favorite_recipe_service.repository.FavoriteRecipeRepository;
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
    private FavoriteRecipeRepository favoriteRecipeRepository;
//    @Autowired
//    private GlobalRecipeService globalRecipeService;
    @Autowired
//    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(FavoriteRecipeService.class);

    public List<Recipe> getFavoritesByUser() {
//        User user = userService.getUserFromToken();

        String username = null; // TODO
        Long userId = null; // TODO

        logger.info("Fetching favorite recipes for user {}.", username);
        Optional<List<Favorite>> favoritesList = favoriteRecipeRepository.getFavoritesByUser(userId);
        return favoritesList.map(favorites -> favorites.stream()
                        .map(Favorite::getRecipe)
                        .collect(Collectors.toList()))
                .map(list -> {
                    Collections.reverse(list);
                    return list;
                })
                .orElse(null);
    }

    public void addFavoriteRecipe(Long recipeId) {
//        User user = userService.getUserFromToken();
        Long userId = null; //TODO
//        Recipe recipe = globalRecipeService.getRecipeById(recipeId);
        validateRecipeNotAddedAsFavorite(recipeId, userId);

        logger.info("Adding recipe {} as favorite to user {}.", recipeId, userId);
        Favorite favorite = new Favorite(recipeId, userId);
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
