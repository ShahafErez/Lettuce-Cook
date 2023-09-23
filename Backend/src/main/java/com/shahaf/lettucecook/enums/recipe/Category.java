package com.shahaf.lettucecook.enums.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Category {
    @JsonProperty("dinner")
    DINNER,
    @JsonProperty("lunch")
    LUNCH,
    @JsonProperty("breakfast")
    BREAKFAST,
    @JsonProperty("salad")
    SALAD,
    @JsonProperty("snack")
    SNACK,
    @JsonProperty("dessert")
    DESSERT
}
