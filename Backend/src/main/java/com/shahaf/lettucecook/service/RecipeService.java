package com.shahaf.lettucecook.service;

import com.shahaf.lettucecook.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private com.shahaf.lettucecook.reposetory.RecipesRepository RecipesRepository;

    public List<Recipe> getAll(){
        return RecipesRepository.findAll();
    }

    public void addRecipe(Recipe recipe){
        RecipesRepository.save(recipe);
    }

}
