package com.shahaf.recipe_service.dto;

import com.shahaf.recipe_service.enums.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {
    @NotBlank
    private String name;
    @NotNull
    private Unit unit;
    @NotNull
    private Float amount;
}
