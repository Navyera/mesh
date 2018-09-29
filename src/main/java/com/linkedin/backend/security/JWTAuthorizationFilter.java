package com.linkedin.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.linkedin.backend.entities.user.AppUserService;
import com.linkedin.backend.entities.user.Role;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.linkedin.backend.security.SecurityConstants.HEADER_STRING;
import static com.linkedin.backend.security.SecurityConstants.SECRET;
import static com.linkedin.backend.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private AppUserService appUserService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AppUserService appUserService) {
        super(authenticationManager);
        this.appUserService = appUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        // If the reqeust has an authorization header
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            try {
                UsernamePasswordAuthenticationToken authentication = getAuth(request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (TokenExpiredException ex) {
                throw new AuthorizationServiceException("EXPIRED");
            }
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuth(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            String user = JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();

            if (user != null) {
                String role = JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                        .build()
                        .verify(token.replace(TOKEN_PREFIX, ""))
                        .getClaim("ROLE").asString();

                if (role != null) {
                    ArrayList<Role> roles = new ArrayList<>();
                    roles.add(new Role(role));


                    return new UsernamePasswordAuthenticationToken(user, null, roles);
                }
            }
        }

        return null;
    }
}
