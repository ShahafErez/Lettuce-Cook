package com.shahaf.lettucecook.entity.recipe;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("recipes")
public class RecipeRedis implements Serializable {
    @Id
    private Long id;
    private String name;
    private String summary;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean dairyFree;
}
