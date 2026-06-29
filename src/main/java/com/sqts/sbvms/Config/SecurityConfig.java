package com.sqts.sbvms.Config;

import com.sqts.sbvms.Security.JwtAuthenticationFilter;
import com.sqts.sbvms.Security.JwtService;
import com.sqts.sbvms.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/register", "/auth/login", "/vendor/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/services/**/vendors").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/services").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/services/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/services/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/services/count").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/vendor").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/vendor/assignService").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendor").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendor/*/services").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendor/*").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/vendor/*").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/vendor/*").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/vendor/*/services/*").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/vendor/*/services/*").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendors/search").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendor/*/services/*").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendor/*/summary").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendors/count").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendor/*/services/*/exists").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/vendor/*/services/bulk").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/vendor/*/status").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/bookings").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/bookings/pending").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/bookings/*/available-vendors").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/bookings/*/assign-vendor").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/bookings/*").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/*/bookings").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/bookings/*/status").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendors/*/bookings").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/bookings").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/bookings/count").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/me/bookings").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/vendors/pending").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendors/*/verification").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/vendors/*/approve").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/vendors/*/reject").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/me").hasAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.PUT, "/me").hasAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.GET, "/me/dashboard").hasAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.GET, "/me/services").hasAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.GET, "/me/services/*").hasAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.POST, "/bookings").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/me/bookings/*").hasAuthority("ROLE_CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/me/vendor/bookings").hasAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.PATCH, "/me/bookings/*/status").hasAuthority("ROLE_VENDOR")

                    .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService){
        return new JwtAuthenticationFilter(
                jwtService,
                customUserDetailsService
        );
    }
}
