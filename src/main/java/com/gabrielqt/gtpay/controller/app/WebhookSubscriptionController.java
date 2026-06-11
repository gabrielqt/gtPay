package com.gabrielqt.gtpay.controller.app;


import com.gabrielqt.gtpay.dto.request.WebhookSubscriptionRequest;
import com.gabrielqt.gtpay.dto.response.WebhookSubscriptionResponse;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.service.WebhookSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/webhooksubscription")
@RequiredArgsConstructor
public class WebhookSubscriptionController {

    private final WebhookSubscriptionService webhookSubscriptionService;

    @PostMapping("/create-webhook-subscription")
    public ResponseEntity<WebhookSubscriptionResponse> createWebhookSubscription(@RequestBody @Valid WebhookSubscriptionRequest request,
                                                                                 Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(webhookSubscriptionService.createWebhookSubscription(request, user));
    }

    @GetMapping
    public Page<WebhookSubscriptionResponse> findAll(
            Authentication authentication,
            Pageable pageable
    ) {
        User user = (User) authentication.getPrincipal();
        return webhookSubscriptionService.findAll(pageable, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebhookSubscription(@PathVariable Long id) {
        webhookSubscriptionService.deleteWebhookSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
