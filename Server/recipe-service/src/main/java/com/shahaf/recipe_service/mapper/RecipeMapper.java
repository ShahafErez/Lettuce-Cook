package com.shahaf.recipe_service.mapper;

import com.shahaf.recipe_service.dto.RecipeCreationDto;
import com.shahaf.recipe_service.entity.Recipe;
import com.shahaf.recipe_service.service.ImageService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Mapper(componentModel = "spring")
public abstract class RecipeMapper {

    @Autowired
    protected ImageService imageService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pictureData", expression = "java(imageService.getImageBytesFromUrl(recipeCreationDto.getPictureUrl()))")
    public abstract Recipe recipeDtoToRecipe(RecipeCreationDto recipeCreationDto) throws IOException;
}
