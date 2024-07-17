package com.shahaf.api_gateway.constants;

public class JwtConstants {
    private JwtConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String SECRET_KEY = "7a4e3f39c2df76ebdf357b64999c8c49e71c870c6cdaa8d79c149d5f981b2d59";
    public static final String AUTH_HEADER_PREFIX = "Bearer ";
    public static final long TOKEN_EXPIRATION_TIME = Integer.MAX_VALUE;
    public static final String ALLOWED_CLIENT_URL = "http://localhost:3000";
}