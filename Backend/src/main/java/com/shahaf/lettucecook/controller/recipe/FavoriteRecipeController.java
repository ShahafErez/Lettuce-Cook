package com.shahaf.lettucecook.controller.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeFavoriteDto;
import com.shahaf.lettucecook.service.recipe.FavoriteRecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/favorite")
public class FavoriteRecipeController {

    @Autowired
    private FavoriteRecipeService favoriteRecipeService;

    @PostMapping("/add")
    public ResponseEntity<String> addRecipeFavorite(@Valid @RequestBody RecipeFavoriteDto recipeFavoriteDto) {
        favoriteRecipeService.addRecipeFavorite(recipeFavoriteDto);
        return new ResponseEntity<>("Recipes successfully added to favorites.", HttpStatus.CREATED);
    }
}
