package com.shahaf.lettucecook.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoriteRecipeDto {
    @NotBlank
    private String recipeId;
    @NotBlank
    private String username;
}
