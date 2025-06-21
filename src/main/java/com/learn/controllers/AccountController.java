package com.learn.controllers;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.learn.entities.User;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import com.learn.models.LoginDto;
import com.learn.models.RegisterDto;
import com.learn.repositories.UserRepository;
import com.learn.security.CustomUserDetails;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {
        @Value("${security.jwt.secret-key}")
        private String jwtSecretKey;

        @Value("${security.jwt.issuer}")
        private String jwtIssuer;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private AuthenticationManager authenticationManager;

        @GetMapping("/profile")
        public ResponseEntity<Object> profile(Authentication auth) {
                Map<String, Object> response = new HashMap<>();
                response.put("Username", auth.getName());
                response.put("Authorities:", auth.getAuthorities());

                var user = userRepository.findByUsername(auth.getName());
                response.put("User", user);

                return ResponseEntity.ok(response);
        }

        @PostMapping("/register")
        public ResponseEntity<Object> register(
                        @Valid @RequestBody RegisterDto registerDto, BindingResult result) {

                if (result.hasErrors()) {
                        var errorList = result.getAllErrors();
                        var errorMap = new HashMap<String, String>();

                        for (int i = 0; i < errorList.size(); i++) {
                                var error = (FieldError) errorList.get(i);
                                errorMap.put(error.getField(), error.getDefaultMessage());
                        }

                        return ResponseEntity.badRequest().body(errorMap);
                }

                var bCryptEncoder = new BCryptPasswordEncoder();
                User user = new User();
                user.setUsername(registerDto.getUsername());
                user.setName(registerDto.getName());
                user.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
                user.setRole("client");
                System.out.print(user);

                try {
                        // Check username availablity
                        var otherUser = userRepository.findByUsername(registerDto.getUsername());
                        if (otherUser != null) {
                                return ResponseEntity.badRequest().body("Username already used");
                        }

                        userRepository.save(user);

                        String jwtToken = createJwtToken(user);

                        Map<String, Object> response = new HashMap<>();
                        response.put("token", jwtToken);
                        response.put("user", Map.of(
                                        "username", user.getUsername(),
                                        "name", user.getName(),
                                        "role", user.getRole()));

                        return ResponseEntity.ok(response);

                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Internal error: " + e.getMessage());

                }
        }

        @PostMapping("/login")
        public ResponseEntity<Object> Login(
                        @Valid @RequestBody LoginDto loginDto, BindingResult result) {
                if (result.hasErrors()) {
                        var errorList = result.getAllErrors();
                        var errorMap = new HashMap<String, String>();

                        for (int i = 0; i < errorList.size(); i++) {
                                var error = (FieldError) errorList.get(i);
                                errorMap.put(error.getField(), error.getDefaultMessage());
                        }

                        return ResponseEntity.badRequest().body(errorMap);
                }

                try {
                        System.out.println("Trying authentication...");
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                                                        loginDto.getPassword()));
                        System.out.println("Authentication succeeded");

                        // User user = userRepository.findByUsername(loginDto.getUsername());
                        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                        User user = userDetails.getUser();

                        String jwtToken = createJwtToken(user);

                        Map<String, Object> response = new HashMap<>();
                        response.put("token", jwtToken);
                        response.put("user", Map.of(
                                        "username", user.getUsername(),
                                        "name", user.getName(),
                                        "role", user.getRole()));

                        return ResponseEntity.ok(response);

                } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Internal error: " + e.getMessage());
                }
        }

        private String createJwtToken(User user) {
                Instant now = Instant.now();

                JwtClaimsSet claims = org.springframework.security.oauth2.jwt.JwtClaimsSet.builder()
                                .issuer(jwtIssuer)
                                .issuedAt(now)
                                .expiresAt(now.plusSeconds(24 * 3600))
                                .subject(user.getUsername())
                                .claim("role", user.getRole())
                                .build();

                var encoder = new NimbusJwtEncoder(
                                new ImmutableSecret<>(jwtSecretKey.getBytes()));

                var params = JwtEncoderParameters.from(
                                JwsHeader.with(MacAlgorithm.HS256).build(), claims);

                return encoder.encode(params).getTokenValue();
        }
}
