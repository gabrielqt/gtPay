package com.gabrielqt.gtpay.controller;

import com.gabrielqt.gtpay.dto.request.LoginRequest;
import com.gabrielqt.gtpay.dto.request.RegisterRequest;
import com.gabrielqt.gtpay.dto.response.TokenResponse;
import com.gabrielqt.gtpay.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest registerDTO) {
        authService.register(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}