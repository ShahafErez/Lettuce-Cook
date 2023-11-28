package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.FavoriteRecipeDto;
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

    public List<Recipe> getFavoritesByUser(String username) {
        Long userId = userService.getUserByUsername(username).getId();

        logger.info("Fetching favorite recipes for user {}.", username);
        Optional<List<Favorite>> favoritesList = favoriteRecipeRepository.getFavoritesByUser(userId);
        return favoritesList.map(favorites -> favorites.stream().map(Favorite::getRecipe).collect(Collectors.toList())).orElse(null);
    }

    public void addFavoriteRecipe(String username, Long recipeId) {
        User user = userService.getUserByUsername(username);
        Recipe recipe = globalRecipeService.getRecipeById(recipeId);
        validateRecipeNotAddedAsFavorite(recipe.getId(), user.getId(), username);

        logger.info("Adding recipe {} as favorite to user {}.", recipe.getId(), user.getActualUsername());
        favoriteRecipeRepository.save(createNewFavoriteObject(user, recipe));
    }

    private void validateRecipeNotAddedAsFavorite(Long recipeId, Long userId, String username) {
        if (favoriteRecipeRepository.findByRecipeIdAndUserId(recipeId, userId).isPresent()) {
            String errorMessage = String.format("Recipe %d already saved as favorite by user %s", recipeId, username);
            logger.error(errorMessage);
            throw new ResourceAlreadyExistsException(errorMessage);
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
        logger.info("Removing all favorites for recipe {}.", recipeId);
        favoriteRecipeRepository.deleteAllFavoritesByRecipe(recipeId);
    }

    @Transactional
    public void removeFavoriteRecipe(FavoriteRecipeDto favoriteRecipeDto) {
        String username = favoriteRecipeDto.getUsername();
        Long recipeId = favoriteRecipeDto.getRecipeId();

        User user = userService.getUserByUsername(username);
        globalRecipeService.validateRecipeExists(recipeId);
        globalRecipeService.validateRecipeIsFavoriteByUser(recipeId, user);

        logger.info("Removing recipe {} from favorites for user {}.", recipeId, username);
        favoriteRecipeRepository.deleteByRecipeIdAndUserId(recipeId, user.getId());

    }

    public void deleteAllFavorites() {
        logger.info("Removing all favorites from DB");
        favoriteRecipeRepository.deleteAll();
    }
}
