package com.shahaf.lettucecook.controller.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.service.recipe.ElasticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.shahaf.lettucecook.constants.ApplicationConstants.PATH_PREFIX;
import static com.shahaf.lettucecook.utils.EnumConverter.stringToEnum;
@Tag(name = "Search", description = "APIs for searching and filtering recipes")
@RestController
@RequestMapping(path = PATH_PREFIX + "/search")
public class SearchController {

    @Autowired
    ElasticService elasticService;

    @Operation(summary = "Search Recipes",
            description = "Search for recipes based on a search term, with optional filtering by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved matching recipes")
    })
    @GetMapping()
    public ResponseEntity<List<RecipeUserDto>> searchRecipes(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String category) {
        return new ResponseEntity<>(elasticService.searchRecipes(searchTerm, stringToEnum(category, Category.class)), HttpStatus.OK);
    }
}
