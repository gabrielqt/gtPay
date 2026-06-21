package com.gabrielqt.gtpay.controller.internal;

import com.gabrielqt.gtpay.dto.request.SimulatePaymentRequest;
import com.gabrielqt.gtpay.service.PaymentService;
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
    private final PaymentService paymentService;

    @PostMapping("/simulate")
    public ResponseEntity<Void> receivePayment(
            @RequestBody SimulatePaymentRequest simulatePaymentRequest
            )
    {
        paymentService.receivePaymentFromPsp(simulatePaymentRequest);
        return ResponseEntity.ok().build();
    }
}
