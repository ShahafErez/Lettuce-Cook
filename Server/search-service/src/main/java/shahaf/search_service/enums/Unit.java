package shahaf.search_service.enums;

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
