package com.learn.controllers;

import java.time.Instant;
import java.util.HashMap;

import com.learn.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.models.RegisterDto;
import com.learn.repositories.UserRepository;
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

                try {
                        // Check username availablity
                        var otherUser = userRepository.findByUsername(registerDto.getUsername());
                        if (otherUser != null)
                                return ResponseEntity.badRequest().body("Username already used");

                        userRepository.save(user);

                        String jwtToken = createJwtToken(user);

                        var response = new HashMap<String, Object>();
                        response.put("token", jwtToken);
                        response.put("user", user);

                        return ResponseEntity.ok(response);

                } catch (Exception e) {
                        System.out.println("There is an Exception :");
                        e.printStackTrace();
                        return ResponseEntity.internalServerError().body("An error occurred during registration.");
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
