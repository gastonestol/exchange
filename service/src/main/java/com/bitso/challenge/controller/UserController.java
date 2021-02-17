package com.bitso.challenge.controller;

import com.bitso.challenge.security.model.AuthenticationRequest;
import com.bitso.challenge.security.jwt.JwtTokenProvider;
import com.bitso.challenge.security.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController("users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> login(@RequestBody AuthenticationRequest data) {
        try {
            String email = data.getEmail();
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, data.getPassword()));
            String token = jwtTokenProvider.createToken(email);
            Map<Object, Object> model = new HashMap<>();
            model.put("id", ((UserPrincipal) auth.getPrincipal()).getId());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Authorization","Bearer "+token);
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}
