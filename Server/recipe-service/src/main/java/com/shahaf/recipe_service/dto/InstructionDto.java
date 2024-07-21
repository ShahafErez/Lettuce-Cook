package com.shahaf.recipe_service.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstructionDto {
    @NotNull
    @Positive
    @Digits(integer = 5, fraction = 0)
    Integer index;
    @NotBlank
    String description;
}