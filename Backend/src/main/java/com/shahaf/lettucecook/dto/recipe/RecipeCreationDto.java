package com.shahaf.lettucecook.dto.recipe;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private List<InstructionDto> instructions;
    private String pictureUrl;
}
