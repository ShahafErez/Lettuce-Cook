package com.shahaf.lettucecook.dto.recipe;

import com.shahaf.lettucecook.enums.recipe.Unit;
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
