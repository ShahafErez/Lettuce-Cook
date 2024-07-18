package shahaf.search_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import shahaf.search_service.enums.Category;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "recipes")
public class RecipeElastic {
    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Text)
    private String summary;
    @Field(type = FieldType.Boolean)
    private Boolean vegetarian;
    @Field(type = FieldType.Boolean)
    private Boolean vegan;
    @Field(type = FieldType.Boolean)
    private Boolean glutenFree;
    @Field(type = FieldType.Boolean)
    private Boolean dairyFree;
    @Field(type = FieldType.Integer)
    private Integer makingTime;
    @Field(type = FieldType.Keyword)
    private List<Category> categories;
    @Field(type = FieldType.Keyword)
    private List<String> ingredients;
    @Field(type = FieldType.Binary)
    private byte[] pictureData;
}