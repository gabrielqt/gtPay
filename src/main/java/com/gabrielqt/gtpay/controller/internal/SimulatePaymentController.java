package com.gabrielqt.gtpay.controller.internal;

import com.gabrielqt.gtpay.dto.request.SimulatePaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/payment/")
@RequiredArgsConstructor
public class SimulatePaymentController {

    @PostMapping("/simulate")
    public ResponseEntity<Void> receivePayment(
            @RequestBody SimulatePaymentRequest simulatePaymentRequest
            )
    {
        return null;
    }
}
