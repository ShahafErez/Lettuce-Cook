package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.mapper.RecipeMapper;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipesRepository recipesRepository;
    @Autowired
    private RecipeGlobalService recipeGlobalService;
    @Autowired
    private FavoriteRecipeService favoriteRecipeService;

    public List<Recipe> getAll() {
        return recipesRepository.findAll();
    }


    public Recipe addRecipe(RecipeCreationDto recipeCreationDto) {
        Recipe recipeCreation = RecipeMapper.MAPPER.recipeDtoToRecipe(recipeCreationDto);
        return recipesRepository.save(recipeCreation);
    }

    public void deleteRecipe(Long recipeId) {
        Recipe recipe = recipeGlobalService.getRecipeById(recipeId);
        if (recipe != null) {
            favoriteRecipeService.deleteAllFavoritesByRecipe(recipeId);
            recipesRepository.deleteById(recipeId);
        }
    }

    public Recipe getRecipeById(Long recipeId) {
        return recipeGlobalService.getRecipeById(recipeId);
    }
}