package com.shahaf.lettucecook.entity.recipe;

import com.shahaf.lettucecook.enums.recipe.Category;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@AllArgsConstructor
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