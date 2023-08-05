package com.shahaf.lettucecook.dto.recipe;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InstructionsDto {
    @NotNull
    @Min(0)
    Long index;
    @NotBlank
    String description;
}