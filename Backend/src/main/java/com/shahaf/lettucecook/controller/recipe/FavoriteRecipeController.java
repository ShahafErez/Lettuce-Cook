package com.shahaf.lettucecook.controller.recipe;

import com.shahaf.lettucecook.dto.recipe.FavoriteRecipeDto;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.service.recipe.FavoriteRecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shahaf.lettucecook.constants.ApplicationConstants.PATH_PREFIX;

@RestController
@RequestMapping(path = PATH_PREFIX + "/favorite")
public class FavoriteRecipeController {

    @Autowired
    private FavoriteRecipeService favoriteRecipeService;

    @GetMapping("/get/{username}")
    public ResponseEntity<List<Recipe>> getFavoritesByUser(@PathVariable String username) {
        return new ResponseEntity<>(favoriteRecipeService.getFavoritesByUser(username), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFavoriteRecipe(@Valid @RequestBody FavoriteRecipeDto favoriteRecipeDto) {
        favoriteRecipeService.addFavoriteRecipe(favoriteRecipeDto);
        return new ResponseEntity<>("Recipes successfully added to favorites.", HttpStatus.CREATED);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFavoriteRecipe(@Valid @RequestBody FavoriteRecipeDto favoriteRecipeDto) {
        favoriteRecipeService.removeFavoriteRecipe(favoriteRecipeDto);
        return new ResponseEntity<>(String.format("Recipes %d successfully removed from favorites for user %s.",
                favoriteRecipeDto.getRecipeId(), favoriteRecipeDto.getUsername()), HttpStatus.OK);
    }
}
