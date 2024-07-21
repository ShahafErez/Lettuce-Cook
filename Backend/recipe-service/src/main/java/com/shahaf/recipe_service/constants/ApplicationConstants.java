package com.shahaf.recipe_service.constants;

public class ApplicationConstants {
    private ApplicationConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String PATH_PREFIX = "api/v1";
    public static final String CACHE_RECIPES = "recipes";
    public static final String SEARCH_SERVICE_URL = "http://localhost:8086/api/v1/search";
}
