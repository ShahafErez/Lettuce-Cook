package com.shahaf.lettucecook.controller.recipe;

import com.shahaf.lettucecook.dto.recipe.FavoriteRecipeDto;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.service.recipe.FavoriteRecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shahaf.lettucecook.constants.ApplicationConstants.PATH_PREFIX;

@Tag(name = "Favorite Recipes", description = "APIs for managing favorite recipes.")
@RestController
@RequestMapping(path = PATH_PREFIX + "/favorite")
public class FavoriteRecipeController {
    @Autowired
    private FavoriteRecipeService favoriteRecipeService;

    @Operation(summary = "Get Favorite Recipes",
            description = "Retrieve all favorite recipes for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user's favorite recipes."),
            @ApiResponse(responseCode = "404", description = "User by the given username was not found.")
    })
    @GetMapping("/get/{username}")
    public ResponseEntity<List<Recipe>> getFavoritesByUser(@PathVariable String username) {
        return new ResponseEntity<>(favoriteRecipeService.getFavoritesByUser(username), HttpStatus.OK);
    }

    @Operation(summary = "Add Recipe to Favorites",
            description = "Add recipe to a user's favorites.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added recipe to user's favorites."),
            @ApiResponse(responseCode = "404", description = "User or recipe were not found."),
            @ApiResponse(responseCode = "409", description = "Recipe was already added as favorite by user.")
    })
    @PostMapping("/add")
    public ResponseEntity<String> addFavoriteRecipe(@Valid @RequestBody FavoriteRecipeDto favoriteRecipeDto) {
        String username = favoriteRecipeDto.getUsername();
        Long recipeId = favoriteRecipeDto.getRecipeId();
        favoriteRecipeService.addFavoriteRecipe(username, recipeId);
        return new ResponseEntity<>(String.format("Recipe %d successfully added to favorites by user %s.", recipeId, username), HttpStatus.CREATED);
    }

    @Operation(summary = "Remove Recipe from Favorites",
            description = "Remove a recipe from user's favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removed recipe from user's favorites"),
            @ApiResponse(responseCode = "404", description = "User not found, recipe does not exist, or recipe is not a favorite by user")
    })
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFavoriteRecipe(@Valid @RequestBody FavoriteRecipeDto favoriteRecipeDto) {
        favoriteRecipeService.removeFavoriteRecipe(favoriteRecipeDto);
        return new ResponseEntity<>(String.format("Recipe %d successfully removed from favorites for user %s.",
                favoriteRecipeDto.getRecipeId(), favoriteRecipeDto.getUsername()), HttpStatus.OK);
    }
}

