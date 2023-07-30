package com.shahaf.lettucecook.controller;

import com.shahaf.lettucecook.dto.RecipeCreationDto;
import com.shahaf.lettucecook.entity.Recipe;
import com.shahaf.lettucecook.service.RecipeService;
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
    public List<Recipe> getAllRecipes() {
        return recipeService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRecipe(@RequestBody RecipeCreationDto recipeCreationDto) {
//        Recipe recipe = mapper.toRecipe(recipeCreationDto);
//        recipeService.addRecipe(recipe);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

}
