package com.shahaf.lettucecook.controller.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.service.recipe.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.shahaf.lettucecook.constants.ApplicationConstants.PATH_PREFIX;
import static utils.EnumConverter.stringToEnum;

@RestController
@RequestMapping(path = PATH_PREFIX + "/search")
public class SearchController {
    @Autowired
    ElasticService elasticService;

    @GetMapping()
    public ResponseEntity<List<RecipeUserDto>> searchByTerm(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String category) {
        return new ResponseEntity<>(elasticService.searchByTerm(searchTerm, stringToEnum(category, Category.class)), HttpStatus.OK);
    }
}
