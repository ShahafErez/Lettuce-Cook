package com.shahaf.recipe_service.controller;

import com.shahaf.recipe_service.dto.FavoriteDto;
import com.shahaf.recipe_service.entity.Recipe;
import com.shahaf.recipe_service.service.FavoriteRecipeService;
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

import static com.shahaf.recipe_service.constants.ApplicationConstants.PATH_PREFIX;


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
    @GetMapping("/get/{username}")
    public ResponseEntity<List<Recipe>> getFavoritesByUser(@PathVariable String username) {
        return new ResponseEntity<>(favoriteRecipeService.getFavoritesByUser(username), HttpStatus.OK);
    }

    @Operation(summary = "Add Recipe to Favorites",
            description = "Add recipe to a user's favorites.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added recipe to user's favorites."),
            @ApiResponse(responseCode = "404", description = "Recipe by the given recipe id was not found."),
            @ApiResponse(responseCode = "409", description = "Recipe was already added as favorite by user.")
    })
    @PostMapping("/add")
    public ResponseEntity<String> addRecipeToFavorites(@Valid @RequestBody FavoriteDto favoriteDto) {
        Long recipeId = favoriteDto.getRecipeId();
        String username = favoriteDto.getUsername();
        favoriteRecipeService.addRecipeToFavorites(recipeId, username);
        return new ResponseEntity<>(String.format("Recipe %d successfully added as favorite by user %s.", recipeId, username), HttpStatus.CREATED);
    }

    @Operation(summary = "Remove Recipe from Favorites",
            description = "Remove a recipe from user's favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removed recipe from user's favorites"),
            @ApiResponse(responseCode = "404", description = "Recipe by the given recipe ID was not found, or recipe is not a favorite by user")
    })
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeRecipeFromFavorites(@Valid @RequestBody FavoriteDto favoriteDto) {
        Long recipeId = favoriteDto.getRecipeId();
        String username = favoriteDto.getUsername();
        favoriteRecipeService.removeRecipeFromFavorites(recipeId, username);
        return new ResponseEntity<>(String.format("Recipe %d successfully removed as favorite by user %s.", recipeId, username), HttpStatus.OK);
    }
}

