package com.shahaf.recipe_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite", uniqueConstraints = {@UniqueConstraint(columnNames = {"recipe_id", "user_id"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue
    private Long id;
    @JoinColumn(name = "recipe_id")
    private Long recipeId;
    private String username;

    public Favorite(Long recipeId, String username) {
        this.recipeId = recipeId;
        this.username = username;
    }
}
