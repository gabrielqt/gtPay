package com.gabrielqt.gtpay.controller;


import com.gabrielqt.gtpay.dto.request.ChargeRequest;
import com.gabrielqt.gtpay.dto.response.ChargeResponse;
import com.gabrielqt.gtpay.repository.ChargeRepository;
import com.gabrielqt.gtpay.service.ChargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/charge")
@RequiredArgsConstructor
public class ChargeController {

    private final ChargeService chargeService;

    @PostMapping("create-chage/")
    public ResponseEntity<ChargeResponse> createCharge(@RequestBody ChargeRequest chargeRequest)
    {
        return ResponseEntity.ok().body(chargeService.createCharge(chargeRequest));
    }
}
