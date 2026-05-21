package com.gabrielqt.gtpay.controller;

import com.gabrielqt.gtpay.dto.request.RequestBaseUrl;
import com.gabrielqt.gtpay.dto.response.MerchantResponse;
import com.gabrielqt.gtpay.mapper.MerchantMapper;
import com.gabrielqt.gtpay.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @PutMapping("/{id}/base-url")
    public ResponseEntity<MerchantResponse> updateBaseUrl(
            @PathVariable Long id,
            @RequestBody @Valid RequestBaseUrl requestBaseUrl
    ) {

        return ResponseEntity.ok(merchantService.updateBaseUrl(requestBaseUrl.baseUrl(), id));

    }
}
