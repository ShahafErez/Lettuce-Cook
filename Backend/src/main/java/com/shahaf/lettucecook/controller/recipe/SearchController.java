package com.shahaf.lettucecook.controller.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.recipe.RecipeElastic;
import com.shahaf.lettucecook.reposetory.elasticsearch.RecipeElasticRepository;
import com.shahaf.lettucecook.service.recipe.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.shahaf.lettucecook.constants.ApplicationConstants.PATH_PREFIX;

@RestController
@RequestMapping(path = PATH_PREFIX + "/search")
public class SearchController {

    @Autowired
    RecipeService recipeService;
    @Autowired
    RecipeElasticRepository recipeElasticRepository;

    @GetMapping("/")
    public List<RecipeUserDto> getRecipes() {
        return recipeService.getRecipes(null, null, false);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<RecipeElastic>> getAllRecipes() {
        List<RecipeElastic> recipeElastics = new ArrayList<>();
        recipeElasticRepository.findAll().forEach(recipeElastics::add);
        return new ResponseEntity<>(recipeElastics, HttpStatus.OK);
    }
}
