package com.trackademic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomLoginSuccessHandler loginSuccessHandler;

   @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
               .csrf(csrf -> csrf
                       .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
               )
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/auth/**", "/css/**", "/js/**", "/img/**").permitAll()
                       .requestMatchers("/admin/**").authenticated()
                       .anyRequest().authenticated()
               )
               .formLogin(form -> form
                       .loginPage("/auth/login")
                       .successHandler(loginSuccessHandler)
                       .permitAll()
               )
               .logout(logout -> logout
                       .logoutUrl("/logout")
                       .logoutSuccessUrl("/auth/login?logout")
                       .permitAll()
               );
       return http.build();
}


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
