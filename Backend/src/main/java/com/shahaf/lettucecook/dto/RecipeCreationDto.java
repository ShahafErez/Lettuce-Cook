package com.shahaf.lettucecook.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeCreationDto {
    private String name;
    private List<IngredientDto> ingredients;
}
