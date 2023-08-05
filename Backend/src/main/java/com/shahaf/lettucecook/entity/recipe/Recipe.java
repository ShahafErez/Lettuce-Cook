package com.shahaf.lettucecook.entity.recipe;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "recipes")
@Data
public class Recipe {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean dairyFree;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private List<Ingredient> ingredients;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "recipe_id")
    private List<Instruction> instructions;
    private String pictureUrl;
}
