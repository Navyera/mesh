package com.linkedin.backend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import static com.linkedin.backend.security.SecurityConstants.SECRET;
import static com.linkedin.backend.security.SecurityConstants.TOKEN_PREFIX;

public class JWTUtils {
    private DecodedJWT token;

    public JWTUtils(String token) {
        this.token = JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));
    }

    public Integer getUserID() {
        return token.getClaim("ID").asInt();
    }
}
