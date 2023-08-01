package com.shahaf.lettucecook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeCreationDto {
    private String name;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean dairyFree;
    private List<IngredientDto> ingredients;
    private List<InstructionsDto> instructions;
    private String pictureUrl;
}
