package com.shahaf.lettucecook.mapper;

import com.shahaf.lettucecook.entity.recipe.Ingredient;
import com.shahaf.lettucecook.entity.recipe.Recipe;
import com.shahaf.lettucecook.entity.recipe.RecipeElastic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RecipeElasticMapper {

    @Mapping(source = "ingredients", target = "ingredients", qualifiedByName = "fromIngredientListToStringList")
    public abstract RecipeElastic recipeToElasticRecipe(Recipe recipe);

    @Named("fromIngredientListToStringList")
    List<String> fromIngredientListToStringList(List<Ingredient> ingredientList) {
        List<String> ingredientStrings = new ArrayList<>();
        ingredientList.forEach(ingredient -> ingredientStrings.add(ingredient.getName()));
        return ingredientStrings;
    }

    @Mapping(target = "ingredients", ignore = true)
    public abstract Recipe recipeElasticToRecipe(RecipeElastic recipeElastic);
}
