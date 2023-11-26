package com.shahaf.lettucecook.mapper;

import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.entity.recipe.RecipeRedis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeRedisMapper {

    RecipeRedis recipeToRedisRecipe(Recipe recipe);

}
