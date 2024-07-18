package shahaf.search_service.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shahaf.search_service.entity.Recipe;
import shahaf.search_service.enums.Category;
import shahaf.search_service.service.RecipeElasticService;

import java.util.List;

import static shahaf.search_service.constants.ApplicationConstants.PATH_PREFIX;
import static shahaf.search_service.utils.EnumConverter.stringToEnum;


@Tag(name = "Search", description = "APIs for searching and filtering recipes")
@RestController
@RequestMapping(path = PATH_PREFIX + "/search")
public class SearchController {

    @Autowired
    RecipeElasticService recipeElasticService;

    @Operation(summary = "Search Recipes",
            description = "Search for recipes based on a search term, with optional filtering by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved matching recipes")
    })
    @GetMapping()
    public ResponseEntity<List<Recipe>> searchRecipes(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String category) {
        return new ResponseEntity<>(recipeElasticService.searchRecipes(searchTerm, stringToEnum(category, Category.class)), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> addRecipeToElastic(@Valid @RequestBody Recipe recipe){
        recipeElasticService.addRecipeToElastic(recipe);
        return new ResponseEntity<>("Recipe added successfully to Elasticsearch", HttpStatus.OK);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> removeRecipeFromElastic(@PathVariable Long recipeId){
        recipeElasticService.deleteRecipeById(recipeId);
        return new ResponseEntity<>("Recipe successfully removed from Elasticsearch", HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> removeAllRecipesFromElastic(){
        recipeElasticService.deleteAllRecipes();
        return new ResponseEntity<>("All recipes successfully removed from Elasticsearch", HttpStatus.OK);
    }

}
