package com.learn.config;

// import org.apache.catalina.User; // Removed incorrect import
import com.learn.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.learn.repositories.UserRepository;
import com.learn.security.CustomUserDetails;
import com.learn.services.UserService;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Value("${security.jwt.secret-key}")
        private String jwtSecretKey;

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/").permitAll()
                                                .requestMatchers("/home").permitAll()
                                                .requestMatchers("/account").permitAll()
                                                .requestMatchers("/account/login").permitAll()
                                                .requestMatchers("/account/register").permitAll()
                                                .anyRequest().authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
                                .build();
        }

        @Bean
        public JwtDecoder jwtDecoder() {
                var secretKey = new SecretKeySpec(jwtSecretKey.getBytes(), "");
                return NimbusJwtDecoder.withSecretKey(secretKey)
                                .macAlgorithm(MacAlgorithm.HS256).build();
        }

        @Bean
        public UserDetailsService userDetailsService(UserRepository userRepository) {
                return username -> {
                        User user = userRepository.findByUsername(username);
                        if (user == null) {
                                throw new UsernameNotFoundException("User not found");
                        }
                        return new CustomUserDetails(user);
                };
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder);
                return provider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}
