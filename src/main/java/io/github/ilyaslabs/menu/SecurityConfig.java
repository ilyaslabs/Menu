package io.github.ilyaslabs.menu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author Muhammad Ilyas (m.ilyas@live.com)
 */
@Configuration
class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(customizer ->
                        customizer
                                .requestMatchers("/api/authenticate").permitAll()
                                .requestMatchers("/api/admin/**").hasAuthority("SCOPE_ADMIN")
                                .requestMatchers("/api/**").permitAll()
                                .anyRequest().permitAll()
                );

        return http.build();
    }
}
