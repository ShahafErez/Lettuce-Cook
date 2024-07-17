package com.shahaf.favorite_recipe_service.controller;

import com.shahaf.favorite_recipe_service.entity.Recipe;
import com.shahaf.favorite_recipe_service.service.FavoriteRecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shahaf.favorite_recipe_service.constants.ApplicationConstants.PATH_PREFIX;


@Tag(name = "Favorite Recipes", description = "APIs for managing favorite recipes.")
@RestController
@RequestMapping(path = PATH_PREFIX + "/favorite")
public class FavoriteRecipeController {
    @Autowired
    private FavoriteRecipeService favoriteRecipeService;

    @Operation(summary = "Get Favorite Recipes",
            description = "Retrieve all favorite recipes for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user's favorite recipes.")})
    @GetMapping("/get")
    public ResponseEntity<List<Recipe>> getFavoritesByUser() {
        return new ResponseEntity<>(favoriteRecipeService.getFavoritesByUser(), HttpStatus.OK);
    }

    @Operation(summary = "Add Recipe to Favorites",
            description = "Add recipe to a user's favorites.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added recipe to user's favorites."),
            @ApiResponse(responseCode = "404", description = "Recipe by the given recipe ID was not found."),
            @ApiResponse(responseCode = "409", description = "Recipe was already added as favorite by user.")
    })
    @PostMapping("/add/{recipeId}")
    public ResponseEntity<String> addFavoriteRecipe(@PathVariable Long recipeId) {
        favoriteRecipeService.addFavoriteRecipe(recipeId);
        return new ResponseEntity<>(String.format("Recipe %d successfully added to favorites by user.", recipeId), HttpStatus.CREATED);
    }

    @Operation(summary = "Remove Recipe from Favorites",
            description = "Remove a recipe from user's favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removed recipe from user's favorites"),
            @ApiResponse(responseCode = "404", description = "Recipe by the given recipe ID was not found, or recipe is not a favorite by user")
    })
    @DeleteMapping("/remove/{recipeId}")
    public ResponseEntity<String> removeFavoriteRecipe(@PathVariable Long recipeId) {
        favoriteRecipeService.removeFavoriteRecipe(recipeId);
        return new ResponseEntity<>(String.format("Recipe %d successfully removed from favorites.", recipeId), HttpStatus.OK);
    }
}

