package com.shahaf.lettucecook.controller.recipe;

import com.shahaf.lettucecook.dto.recipe.FavoriteRecipeDto;
import com.shahaf.lettucecook.entity.recipe.Favorite;
import com.shahaf.lettucecook.service.recipe.FavoriteRecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/favorite")
public class FavoriteRecipeController {

    @Autowired
    private FavoriteRecipeService favoriteRecipeService;

    @PostMapping("/add")
    public ResponseEntity<String> addFavoriteRecipe(@Valid @RequestBody FavoriteRecipeDto favoriteRecipeDto) {
        favoriteRecipeService.addFavoriteRecipe(favoriteRecipeDto);
        return new ResponseEntity<>("Recipes successfully added to favorites.", HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Favorite>> addFavoriteRecipe(@PathVariable String username) {
        return new ResponseEntity<>(favoriteRecipeService.getFavoritesByUser(username), HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFavoriteRecipe(@Valid @RequestBody FavoriteRecipeDto favoriteRecipeDto) {
        String recipeId = favoriteRecipeDto.getRecipeId();
        String username = favoriteRecipeDto.getUsername();
        favoriteRecipeService.removeFavoriteRecipe(Long.valueOf(recipeId), username);
        return new ResponseEntity<>(String.format("Recipes %s successfully removed from favorites for user %s.", recipeId, username), HttpStatus.OK);
    }
}
