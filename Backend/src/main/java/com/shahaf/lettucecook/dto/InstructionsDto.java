package com.shahaf.lettucecook.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InstructionsDto {
    @NotNull
    @Min(0)
    Long index;
    @NotEmpty
    String description;
}