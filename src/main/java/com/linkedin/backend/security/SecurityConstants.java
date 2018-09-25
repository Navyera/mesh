package com.linkedin.backend.security;

public class SecurityConstants {
    public static final String SECRET = "EZCLAP";
    public static final long EXPIRATION_TIME = 2_200_000; // 10 minutes
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/users/register";
}