package com.shahaf.recipe_service.service;

import com.shahaf.recipe_service.entity.Recipe;
import com.shahaf.recipe_service.exceptions.ResourceNotFound;
import com.shahaf.recipe_service.repository.RecipesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeHelperService {

    @Autowired
    RecipesRepository recipesRepository;
    private static final Logger logger = LoggerFactory.getLogger(RecipeHelperService.class);


    public void validateRecipeExists(Long recipeId) {
        logger.info("Checking if recipe {} exists in DB.", recipeId);
        if (!recipesRepository.existsById(recipeId)) {
            String errorMessage = String.format("Recipe %d was not found.", recipeId);
            logger.error(errorMessage);
            throw new ResourceNotFound(errorMessage);
        }
    }

    public List<Recipe> getRecipesById(List<Long> recipeIds) {
        return recipesRepository.findByIdIn(recipeIds);
    }


}


