package com.siddhant.backend.security.config;

import com.siddhant.backend.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration @RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                .requestMatchers("/api/auth/me").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/jobs", "/api/jobs/**", "/api/companies", "/api/companies/**").authenticated()
                .requestMatchers("/api/jobs", "/api/jobs/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/companies", "/api/companies/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/companies", "/api/companies/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/companies", "/api/companies/**").hasAuthority("ADMIN")
                .requestMatchers("/api/dashboard/stats").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/students").hasAuthority("ADMIN")
                .requestMatchers("/api/students/**").hasAuthority("STUDENT")
                .requestMatchers("/api/resumes/**").hasAuthority("STUDENT")
                .requestMatchers(HttpMethod.PUT, "/api/job-applications", "/api/job-applications/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/job-applications", "/api/job-applications/**").hasAuthority("STUDENT")
                .anyRequest().authenticated())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:3000", "http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Content-Disposition"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
