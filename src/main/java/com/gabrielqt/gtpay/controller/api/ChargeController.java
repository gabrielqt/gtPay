package com.gabrielqt.gtpay.controller.api;


import com.gabrielqt.gtpay.dto.request.ChargeRequest;
import com.gabrielqt.gtpay.dto.response.ChargeResponse;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.service.ChargeService;
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
    public ResponseEntity<Void> newCharge(@RequestBody ChargeRequest chargeRequest,
                                                    Authentication authentication
                                                    ) {
        Merchant merchant = (Merchant) authentication.getPrincipal();
        chargeService.newCharge(chargeRequest, merchant);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
