package com.shahaf.lettucecook.mapper;

import com.shahaf.lettucecook.dto.recipe.RecipeCreationDto;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeMapper {
    RecipeMapper MAPPER = Mappers.getMapper(RecipeMapper.class);

    @Mapping(target = "id", ignore = true)
    Recipe recipeDtoToRecipe(RecipeCreationDto recipeCreationDto);
}
