package com.shahaf.lettucecook.service;

import com.shahaf.lettucecook.dto.RecipeCreationDto;
import com.shahaf.lettucecook.entity.Recipe;
import com.shahaf.lettucecook.mapper.RecipeMapper;
import com.shahaf.lettucecook.reposetory.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipesRepository recipesRepository;

    public List<Recipe> getAll() {
        return recipesRepository.findAll();
    }

    public Recipe addRecipe(RecipeCreationDto recipeCreationDto) {
        Recipe recipeCreation = RecipeMapper.MAPPER.recipeDtoToRecipe(recipeCreationDto);
        return recipesRepository.save(recipeCreation);
    }

}
