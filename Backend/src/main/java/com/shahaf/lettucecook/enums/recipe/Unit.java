package com.shahaf.lettucecook.enums.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Unit {
    @JsonProperty("cup")
    CUP,
    @JsonProperty("spoon")
    SPOON,
    @JsonProperty("piece")
    PIECE,
    @JsonProperty("gram")
    GRAM;
}
