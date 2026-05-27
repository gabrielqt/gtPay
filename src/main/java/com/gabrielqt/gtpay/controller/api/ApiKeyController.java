package com.gabrielqt.gtpay.controller.api;

import com.gabrielqt.gtpay.dto.response.ApiKeyResponse;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-key")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping("/generate")
    public ResponseEntity<ApiKeyResponse> login(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(apiKeyService.generateApiKey(user));
    }

}