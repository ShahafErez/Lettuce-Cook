package com.shahaf.lettucecook.controller;

import com.shahaf.lettucecook.dto.RecipeCreationDto;
import com.shahaf.lettucecook.entity.Recipe;
import com.shahaf.lettucecook.service.RecipeService;
import jakarta.validation.Valid;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/add")
    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody RecipeCreationDto recipeCreationDto) {
        Recipe newSavedRecipe = recipeService.addRecipe(recipeCreationDto);
        return new ResponseEntity<>(newSavedRecipe, HttpStatus.CREATED);
    }

}
