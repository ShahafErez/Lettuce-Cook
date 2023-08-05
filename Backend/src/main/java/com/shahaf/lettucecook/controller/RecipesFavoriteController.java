package com.shahaf.lettucecook.controller;

import com.shahaf.lettucecook.dto.RecipeFavoriteDto;
import com.shahaf.lettucecook.service.RecipeFavoriteService;
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
public class RecipesFavoriteController {

    @Autowired
    private RecipeFavoriteService recipeFavoriteService;

    @PostMapping("/add")
    public ResponseEntity<String> addRecipeFavorite(@Valid @RequestBody RecipeFavoriteDto recipeFavoriteDto) {
        recipeFavoriteService.addRecipeFavorite(recipeFavoriteDto);
        return new ResponseEntity<>("Recipes successfully added to favorites.", HttpStatus.CREATED);
    }
}
