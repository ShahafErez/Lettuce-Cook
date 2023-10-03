package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.mapper.RecipeMapper;
import com.shahaf.lettucecook.reposetory.recipe.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipesRepository recipesRepository;
    @Autowired
    private RecipeGlobalService recipeGlobalService;
    @Autowired
    private FavoriteRecipeService favoriteRecipeService;
    @Autowired
    private RecipeMapper recipeMapper;

    public List<Recipe> getAll() {
        return recipesRepository.findAll();
    }

    public Recipe getRecipeById(Long recipeId) {
        return recipeGlobalService.getRecipeById(recipeId);
    }

    public Recipe addRecipe(RecipeCreationDto recipeCreationDto) throws IOException {
        Recipe recipeCreation = recipeMapper.recipeDtoToRecipe(recipeCreationDto);
        return recipesRepository.save(recipeCreation);
    }

    public void deleteRecipe(Long recipeId) {
        Recipe recipe = recipeGlobalService.getRecipeById(recipeId);
        if (recipe != null) {
            favoriteRecipeService.deleteAllFavoritesByRecipe(recipeId);
            recipesRepository.deleteById(recipeId);
        }
    }
}
