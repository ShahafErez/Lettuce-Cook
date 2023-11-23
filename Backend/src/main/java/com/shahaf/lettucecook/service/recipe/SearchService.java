package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.entity.recipe.RecipeElastic;
import com.shahaf.lettucecook.enums.recipe.Category;
import com.shahaf.lettucecook.reposetory.elasticsearch.RecipeElasticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    RecipeElasticRepository recipeElasticRepository;

    public List<RecipeElastic> searchByTerm(String searchTerm, Category category) {
        if (searchTerm == null) {
            if (category == null) {
                return recipeElasticRepository.findAll();
            }
            return recipeElasticRepository.findByCategories(category);
        }
        if (category == null) {
            return recipeElasticRepository.findByNameOrSummaryOrIngredients(searchTerm);
        }
        return recipeElasticRepository.findByNameOrSummaryOrIngredientsAndCategory(searchTerm, category);
    }

}
