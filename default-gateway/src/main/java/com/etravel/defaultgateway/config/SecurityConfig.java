package com.etravel.defaultgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity // enables @PreAuthorize
public class SecurityConfig {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /**
     * Decode & verify incoming JWTs using the shared HMAC secret.
     */
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        SecretKey key = new SecretKeySpec(
                jwtSecret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"    // or "HmacSHA512" if you signed with HS512
        );
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                // disable CSRF (for stateless APIs)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // authorize requests
                .authorizeExchange(authorize -> authorize

                        // 1) Public endpoints
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers(HttpMethod.GET,
                                "/catalog/**",
                                "/reviews/**"
                        ).permitAll()
//                        .pathMatchers(HttpMethod.POST,
//                                "/api/users/register",
//                                "/api/users/login"
//                        ).permitAll()
//
//                        // 2) CLIENT
//                        .pathMatchers(HttpMethod.POST, "/api/reviews").hasRole("CLIENT")
//                        .pathMatchers(HttpMethod.POST, "/api/reservations").hasRole("CLIENT")
//                        .pathMatchers(HttpMethod.GET,  "/api/reservations").hasRole("CLIENT")
//                        .pathMatchers(HttpMethod.DELETE, "/api/reservations/**").hasRole("CLIENT")
//                        .pathMatchers(HttpMethod.GET,  "/api/users/{userId}").hasRole("CLIENT")
//                        .pathMatchers(HttpMethod.PUT,  "/api/users/{userId}").hasRole("CLIENT")
//
//                        // 3) EMPLOYEE
//                        .pathMatchers(HttpMethod.POST,   "/api/tour-packages/**").hasRole("EMPLOYEE")
//                        .pathMatchers(HttpMethod.PUT,    "/api/tour-packages/**").hasRole("EMPLOYEE")
//                        .pathMatchers(HttpMethod.DELETE, "/api/tour-packages/**").hasRole("EMPLOYEE")
//                        .pathMatchers(HttpMethod.GET,    "/api/reservations**").hasRole("EMPLOYEE")
//                        .pathMatchers(HttpMethod.GET,    "/api/users/export**").hasRole("EMPLOYEE")
//
//                        // 4) MANAGER
//                        .pathMatchers("/api/statistics/**").hasRole("MANAGER")
//
//                        // 5) ADMIN
//                        .pathMatchers(HttpMethod.GET,    "/api/users").hasRole("ADMIN")
//                        .pathMatchers(HttpMethod.POST,   "/api/users/**").hasRole("ADMIN")
//                        .pathMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
//
//                        // all other endpoints require auth
//                        .anyExchange().authenticated()
                                .anyExchange().permitAll()
                )

                // JWT-based resource server
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}