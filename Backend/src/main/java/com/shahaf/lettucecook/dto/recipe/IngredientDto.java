package com.shahaf.lettucecook.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IngredientDto {
    @NotBlank
    private String name;
    @NotBlank
    private String unit;
    @NotNull
    private Float amount;

}
