package com.shahaf.lettucecook.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecipeCreationDto {
    @NotBlank
    private String name;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean dairyFree;
    @NotEmpty
    @Valid
    private List<IngredientDto> ingredients;
    @NotEmpty
    @Valid
    private List<InstructionsDto> instructions;
    private String pictureUrl;
}
