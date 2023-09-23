package com.shahaf.lettucecook.dto.recipe;

import com.shahaf.lettucecook.enums.recipe.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    private String summary;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean dairyFree;
    @Positive
    @NotNull
    private Integer makingTime;
    @Positive
    @Digits(integer = 5, fraction = 0)
//    @Size(min = 1, message = "servings should be a natural number")
    @NotNull
    private Integer servings;
    private List<Category> categories;
    private String pictureUrl;
    @NotEmpty
    @Valid
    private List<IngredientDto> ingredients;
    @NotEmpty
    @Valid
    private List<InstructionDto> instructions;
}
