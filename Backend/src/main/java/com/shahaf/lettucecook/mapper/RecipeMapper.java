package com.shahaf.lettucecook.mapper;

import com.shahaf.lettucecook.dto.RecipeCreationDto;
import com.shahaf.lettucecook.entity.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeMapper {
    RecipeMapper MAPPER = Mappers.getMapper(RecipeMapper.class);

    Recipe recipeDtoToRecipe(RecipeCreationDto recipeCreationDto);

}
