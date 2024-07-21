package shahaf.search_service.entity;

import lombok.Data;
import shahaf.search_service.enums.Category;

import java.util.List;

@Data
public class Recipe {
    private Long id;
    private String name;
    private String summary;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean dairyFree;
    private Integer makingTime;
    private Integer servings;
    private List<Category> categories;
    private String pictureUrl;
    private byte[] pictureData;
    private List<Ingredient> ingredients;
    private List<Instruction> instructions;
}
