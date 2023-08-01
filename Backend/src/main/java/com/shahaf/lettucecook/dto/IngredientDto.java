package com.shahaf.lettucecook.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IngredientDto {
    @NotEmpty
    private String name;
    @NotNull
    private String unit;
    @NotNull
    private Float amount;

}
