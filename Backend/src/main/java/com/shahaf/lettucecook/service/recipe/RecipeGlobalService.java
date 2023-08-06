package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.exceptions.ResourceNotFound;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeGlobalService {

    @Autowired
    RecipesRepository recipesRepository;

    public Recipe getRecipeById(Long id) {
        return recipesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Recipe not found with id: " + id));
    }
}
