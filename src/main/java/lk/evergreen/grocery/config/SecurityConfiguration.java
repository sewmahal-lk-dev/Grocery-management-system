package lk.evergreen.grocery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").permitAll() // Admin endpoints separated. Full server-side role check requires JWT/Session token filter.
                        .requestMatchers("/api/users/**", "/api/products/**", "/api/categories/**").permitAll()
                        .anyRequest().permitAll());

        return http.build();
    }
}
