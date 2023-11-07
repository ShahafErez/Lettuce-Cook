package com.shahaf.lettucecook.dto.recipe;

import com.shahaf.lettucecook.entity.recipe.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipeUserDto {
    private Recipe recipe;
    private Boolean isFavoriteByUser;
}
