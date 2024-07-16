package com.shahaf.recipe_service.constants;

public class ApplicationConstants {
    private ApplicationConstants() {
        throw new IllegalStateException("Constants class");
    }

    //    TODO- maybe move to global file
    public static final String PATH_PREFIX = "api/v1";
    public static final String CACHE_RECIPES = "recipes";
}
