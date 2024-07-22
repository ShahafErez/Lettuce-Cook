package com.shahaf.recipe_service.controller;


import com.shahaf.recipe_service.dto.RecipeCreationDto;
import com.shahaf.recipe_service.entity.Recipe;
import com.shahaf.recipe_service.enums.Category;
import com.shahaf.recipe_service.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.shahaf.recipe_service.constants.ApplicationConstants.CACHE_RECIPES;
import static com.shahaf.recipe_service.constants.ApplicationConstants.PATH_PREFIX;
import static com.shahaf.recipe_service.utils.EnumConverter.stringToEnum;

@Tag(name = "Recipes", description = "All APIs related to recipes")
@RestController
@RequestMapping(path = PATH_PREFIX + "/recipes")
public class RecipesController {

    @Autowired
    private RecipeService recipeService;

    @Operation(summary = "Get recipes list",
            description = "Retrieve a list of recipes based on specified parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched matching recipes")
    })
    @GetMapping("/get-recipes")
    public ResponseEntity<List<Recipe>> getRecipes(
            @RequestParam(name = "numOfRecipes", required = false) Integer numOfRecipes,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "random", defaultValue = "false", required = false) Boolean random) {
        return new ResponseEntity<>(recipeService.getRecipes(numOfRecipes, stringToEnum(category, Category.class), random), HttpStatus.OK);
    }

    @Operation(summary = "Get recipe by ID",
            description = "Retrieve a specific recipe by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the matching recipe."),
            @ApiResponse(responseCode = "404", description = "Recipe by the given ID was not found.")
    })
    @GetMapping("/get/{recipeId}")
    @Cacheable(value = CACHE_RECIPES, key = "#recipeId")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable("recipeId") Long recipeId) {
        return new ResponseEntity<>(recipeService.getRecipeById(recipeId), HttpStatus.OK);
    }

    @Operation(summary = "Add Recipe",
            description = "Add a new recipe based on provided fields")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe successfully created"),
            @ApiResponse(responseCode = "422", description = "Failed to add recipe.")
    })
    @PostMapping("/add")
    public ResponseEntity<String> addRecipe(@Valid @RequestBody RecipeCreationDto recipeCreationDto) throws IOException {
        Recipe newSavedRecipe = recipeService.addRecipe(recipeCreationDto);
        return new ResponseEntity<>("Recipe added successfully. New recipe id: " + newSavedRecipe.getId(), HttpStatus.CREATED);
    }

    @Operation(summary = "Add Multiple Recipes",
            description = "Add multiple recipes based on a list of provided fields")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "All recipes successfully created"),
            @ApiResponse(responseCode = "422", description = "Failed to add one or more recipes")
    })
    @PostMapping("/add-multiple")
    public ResponseEntity<String> addRecipe(@Valid @RequestBody List<RecipeCreationDto> recipeCreationDtoList) throws IOException {
        for (RecipeCreationDto recipeCreationDto : recipeCreationDtoList) {
            recipeService.addRecipe(recipeCreationDto);
        }
        return new ResponseEntity<>("All recipes added successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "Delete Recipe",
            description = "Delete a recipe by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Recipe by the given ID was not found")
    })
    @DeleteMapping("/delete/{recipeId}")
    @CacheEvict(cacheNames = CACHE_RECIPES, key = "#recipeId", beforeInvocation = true)
    public ResponseEntity<String> deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return new ResponseEntity<>(String.format("Recipe %s deleted.", recipeId), HttpStatus.OK);
    }

    @Operation(summary = "Delete All Recipes",
            description = "Delete all recipes from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes successfully deleted")
    })
    @DeleteMapping("/delete-all")
    @CacheEvict(cacheNames = CACHE_RECIPES, allEntries = true)
    public ResponseEntity<String> deleteAllRecipes() {
        recipeService.deleteAllRecipes();
        return new ResponseEntity<>("Deleted all recipes.", HttpStatus.OK);
    }
}
