package com.gabrielqt.gtpay.controller;


import com.gabrielqt.gtpay.dto.request.WebhookSubscriptionRequest;
import com.gabrielqt.gtpay.dto.response.WebhookSubscriptionResponse;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.service.WebhookSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooksubscription")
@RequiredArgsConstructor
public class WebhookSubscriptionController {

    private final WebhookSubscriptionService webhookSubscriptionService;

    @PostMapping("/create-webhook-subscription")
    public ResponseEntity<WebhookSubscriptionResponse> createWebhookSubscription(@RequestBody @Valid WebhookSubscriptionRequest request,
                                                                                 Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(webhookSubscriptionService.createWebhookSubscription(request, user));
    }
}
