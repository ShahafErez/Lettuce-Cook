package com.shahaf.favorite_recipe_service.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Category {
    @JsonProperty("dinner")
    DINNER,
    @JsonProperty("lunch")
    LUNCH,
    @JsonProperty("salad")
    SALAD,
    @JsonProperty("snack")
    SNACK,
    @JsonProperty("dessert")
    DESSERT
}
