package com.shahaf.lettucecook.enums.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Unit {
    @JsonProperty("cup")
    CUP,
    @JsonProperty("tablespoon")
    TABLESPOON,
    @JsonProperty("teaspoon")
    TEASPOON,
    @JsonProperty("piece")
    PIECE,
    @JsonProperty("gram")
    GRAM,
    @JsonProperty("kilogram")
    KILOGRAM;
}
