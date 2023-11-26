package com.shahaf.lettucecook.service.recipe;

import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.entity.recipe.RecipeRedis;
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
        recipeRedisRepository.save(recipeRedisMapper.recipeToRedisRecipe(recipe));
    }

    public void deleteRecipeById(Long id) {
        recipeRedisRepository.deleteById(id);
    }

    public void deleteAllRecipes() {
        recipeRedisRepository.deleteAll();
    }

    public Iterable<RecipeRedis> getAllRecipes(){
        return recipeRedisRepository.findAll();
    }
}
