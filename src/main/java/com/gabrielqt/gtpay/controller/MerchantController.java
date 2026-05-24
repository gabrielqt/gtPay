package com.gabrielqt.gtpay.controller;

import com.gabrielqt.gtpay.dto.request.BaseUrlRequest;
import com.gabrielqt.gtpay.dto.response.BaseUrlResponse;
import com.gabrielqt.gtpay.dto.response.MerchantResponse;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.mapper.MerchantMapper;
import com.gabrielqt.gtpay.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @PutMapping("/base-url")
        public ResponseEntity<BaseUrlResponse> updateBaseUrl(
            Authentication authentication,
            @RequestBody @Valid BaseUrlRequest requestBaseUrl
    ) {

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(merchantService.updateBaseUrl(requestBaseUrl.baseUrl(), user));

    }
}
