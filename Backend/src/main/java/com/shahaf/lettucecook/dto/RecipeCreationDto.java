package com.shahaf.lettucecook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecipeCreationDto {
    private String name;
    private List<IngredientDto> ingredients;
}
