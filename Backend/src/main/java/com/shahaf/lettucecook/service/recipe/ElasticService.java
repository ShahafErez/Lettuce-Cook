package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeUserDto;
import com.shahaf.lettucecook.entity.User;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.entity.recipe.RecipeElastic;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.exceptions.ErrorOccurredException;
import com.shahaf.lettucecook.mapper.RecipeElasticMapper;
import com.shahaf.lettucecook.reposetory.elasticsearch.RecipeElasticRepository;
import com.shahaf.lettucecook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticService {
    @Autowired
    RecipeElasticRepository recipeElasticRepository;
    @Autowired
    RecipeElasticMapper recipeElasticMapper;
    @Autowired
    GlobalRecipeService globalRecipeService;
    @Autowired
    UserService userService;

    public void saveRecipe(Recipe recipe) {
        try {
            recipeElasticRepository.save(recipeElasticMapper.recipeToElasticRecipe(recipe));
        } catch (Exception e) {
            throw new ErrorOccurredException("Failed to add recipe to Elastic.");
        }
    }

    public void deleteRecipeById(Long id) {
        recipeElasticRepository.deleteById(id);
    }

    public void deleteAllRecipes() {
        recipeElasticRepository.deleteAll();
    }

    public List<RecipeUserDto> searchByTerm(String searchTerm, Category category) {
        User user = userService.getUserFromToken();
        List<RecipeElastic> queryResult;

        if ((searchTerm == null || searchTerm.isEmpty()) && category == null) {
            queryResult = recipeElasticRepository.findAll();
        } else if (searchTerm == null || searchTerm.isEmpty()) {
            queryResult = recipeElasticRepository.findByCategories(category);
        } else if (category == null) {
            queryResult = recipeElasticRepository.findByNameOrSummaryOrIngredients(searchTerm);
        } else {
            queryResult = recipeElasticRepository.findByNameOrSummaryOrIngredientsAndCategory(searchTerm, category);
        }
        return queryResultToRecipeUserDtoList(queryResult, user);
    }

    private List<RecipeUserDto> queryResultToRecipeUserDtoList(List<RecipeElastic> queryResult, User user) {
        return queryResult.stream().map(recipeElastic -> {
            Recipe recipe = recipeElasticMapper.recipeElasticToRecipe(recipeElastic);
            return globalRecipeService.mapRecipeToRecipeUserDto(recipe, user);
        }).collect(Collectors.toList());
    }
}
