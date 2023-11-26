package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.entity.recipe.RecipeRedis;
import com.shahaf.lettucecook.exceptions.ErrorOccurredException;
import com.shahaf.lettucecook.mapper.RecipeRedisMapper;
import com.shahaf.lettucecook.reposetory.redis.RecipeRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    RecipeRedisRepository recipeRedisRepository;
    @Autowired
    RecipeRedisMapper recipeRedisMapper;

    public void saveRecipe(Recipe recipe) {
        try {
            recipeRedisRepository.save(recipeRedisMapper.recipeToRedisRecipe(recipe));
        } catch (Exception e) {
            throw new ErrorOccurredException("Failed to add recipe to Redis.");
        }
    }

    public void deleteRecipeById(Long id) {
        recipeRedisRepository.deleteById(id);
    }

    public void deleteAllRecipes() {
        recipeRedisRepository.deleteAll();
    }

    public Iterable<RecipeRedis> getAllRecipes() {
        return recipeRedisRepository.findAll();
    }
}
