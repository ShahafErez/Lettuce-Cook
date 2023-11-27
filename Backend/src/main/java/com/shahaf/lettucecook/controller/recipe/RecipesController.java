package com.shahaf.lettucecook.controller.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.service.recipe.RecipeService;
import jakarta.validation.Valid;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.shahaf.lettucecook.constants.ApplicationConstants.CACHE_RECIPES;
import static com.shahaf.lettucecook.constants.ApplicationConstants.PATH_PREFIX;
import static utils.EnumConverter.stringToEnum;

@RestController
@RequestMapping(path = PATH_PREFIX + "/recipes")
public class RecipesController {

    @Autowired
    private RecipeService recipeService;

    private Mapper mapper;

    @GetMapping("/get-recipes")
    public ResponseEntity<List<RecipeUserDto>> getRecipes(
            @RequestParam(required = false) Integer numOfRecipes,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "false", required = false) Boolean random) {
        return new ResponseEntity<>(recipeService.getRecipes(numOfRecipes, stringToEnum(category, Category.class), random), HttpStatus.OK);
    }

    @GetMapping("/get/{recipeId}")
    @Cacheable(value = CACHE_RECIPES, key = "#recipeId")
    public ResponseEntity<RecipeUserDto> getRecipeById(@PathVariable Long recipeId) {
        return new ResponseEntity<>(recipeService.getRecipeById(recipeId), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRecipe(@Valid @RequestBody RecipeCreationDto recipeCreationDto) throws IOException {
        Recipe newSavedRecipe = recipeService.addRecipe(recipeCreationDto);
        return new ResponseEntity<>("Recipe added successfully. New recipe id: " + newSavedRecipe.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/add-many")
    public ResponseEntity<String> addRecipe(@Valid @RequestBody List<RecipeCreationDto> recipeCreationDtoList) throws IOException {
        for (RecipeCreationDto recipeCreationDto : recipeCreationDtoList) {
            recipeService.addRecipe(recipeCreationDto);
        }
        return new ResponseEntity<>("Recipes added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{recipeId}")
    @CacheEvict(cacheNames = CACHE_RECIPES, key = "#recipeId", beforeInvocation = true)
    public ResponseEntity<String> deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return new ResponseEntity<>(String.format("Recipe %s deleted.", recipeId), HttpStatus.OK);
    }

    @DeleteMapping("/delete-all")
    @CacheEvict(cacheNames = CACHE_RECIPES, allEntries = true)
    public ResponseEntity<String> deleteAllRecipes() {
        recipeService.deleteAllRecipes();
        return new ResponseEntity<>("Deleted all recipes.", HttpStatus.OK);
    }
}
