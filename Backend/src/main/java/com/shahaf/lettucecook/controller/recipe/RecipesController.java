package com.shahaf.lettucecook.controller.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.service.recipe.RecipeService;
import jakarta.validation.Valid;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "api/v1/recipes")
public class RecipesController {

    @Autowired
    private RecipeService recipeService;

    private Mapper mapper;

    @GetMapping("/get-all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return new ResponseEntity<>(recipeService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get/{recipeId}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long recipeId) {
        return new ResponseEntity<>(recipeService.getRecipeById(recipeId), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRecipe(@Valid @RequestBody RecipeCreationDto recipeCreationDto) {
        Recipe newSavedRecipe = recipeService.addRecipe(recipeCreationDto);
        return new ResponseEntity<>("Recipe added successfully. New recipe id: " + newSavedRecipe.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return new ResponseEntity<>(String.format("Recipe %s deleted.", recipeId), HttpStatus.OK);
    }

}
