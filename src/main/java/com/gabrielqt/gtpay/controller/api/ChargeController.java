package com.gabrielqt.gtpay.controller.api;


import com.gabrielqt.gtpay.dto.request.ChargeRequest;
import com.gabrielqt.gtpay.dto.response.ChargeResponse;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.service.ChargeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/charge")
@RequiredArgsConstructor
public class ChargeController {

    private final ChargeService chargeService;

    @PostMapping("new-charge/")
    public ResponseEntity<ChargeResponse> newCharge(@RequestBody @Valid ChargeRequest chargeRequest,
                                                    Authentication authentication
                                                    ) {
        Merchant merchant = (Merchant) authentication.getPrincipal();
        return ResponseEntity.ok(chargeService.newCharge(chargeRequest, merchant));
    }
}
