package com.linkedin.backend.security;

import com.linkedin.backend.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private AppUserService appUserService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurity(AppUserService appUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(authenticationManager(), appUserService);
        authenticationFilter.setFilterProcessesUrl("/api/login");

        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                .antMatchers(HttpMethod.GET, "/assets/**", "/*.txt", "/*.js", "/index.html", "/").permitAll()
                .anyRequest().hasRole("USER")
                .and()
                .addFilterAfter(authenticationFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(new JWTAuthorizationFilter(authenticationManager(), appUserService), ExceptionTranslationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserService).passwordEncoder(bCryptPasswordEncoder);
    }
}