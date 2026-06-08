package com.gabrielqt.gtpay.controller.app;

import com.gabrielqt.gtpay.dto.request.BaseUrlRequest;
import com.gabrielqt.gtpay.dto.response.BaseUrlResponse;
import com.gabrielqt.gtpay.dto.response.MerchantResponse;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/merchant")
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

    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponse> findById (@PathVariable Long id) {
        return ResponseEntity.ok(merchantService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<MerchantResponse>> findAll (
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(merchantService.findAll(pageable));
    }
}
