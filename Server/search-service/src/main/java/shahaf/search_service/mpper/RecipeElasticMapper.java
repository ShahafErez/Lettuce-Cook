package shahaf.search_service.mpper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import shahaf.search_service.entity.Ingredient;
import shahaf.search_service.entity.Recipe;
import shahaf.search_service.entity.RecipeElastic;

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
