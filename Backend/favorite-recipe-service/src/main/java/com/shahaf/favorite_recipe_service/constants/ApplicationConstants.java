package com.shahaf.favorite_recipe_service.constants;

public class ApplicationConstants {
    private ApplicationConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String PATH_PREFIX = "api/v1";
    public static final String CACHE_RECIPES = "recipes";
}
