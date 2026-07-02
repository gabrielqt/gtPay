package com.gabrielqt.gtpay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielqt.gtpay.config.RabbitMQConfig;
import com.gabrielqt.gtpay.dto.message.MessageCharge;
import com.gabrielqt.gtpay.dto.request.WebhookPayload;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.WebhookEvent;
import com.gabrielqt.gtpay.entity.WebhookSubscription;
import com.gabrielqt.gtpay.mapper.WebhookPayloadMapper;
import com.gabrielqt.gtpay.security.SecretService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {
    private final RestClient restClient;
    private final MerchantService merchantService;
    private final WebhookSubscriptionService webhookSubscriptionService;
    private final WebhookEventService webhookEventService;
    private final WebhookPayloadMapper payloadMapper;
    private final ObjectMapper objectMapper;
    private final SecretService secretService;
    private final ChargeService chargeService;

    @RabbitListener(queues = RabbitMQConfig.WEBHOOK_QUEUE)
    public void onPaymentConfirmed(MessageCharge message) throws JsonProcessingException {

        log.info("Received payment confirmation for charge {}", message.chargeId());
        Merchant merchant = merchantService.findMerchantById(message.merchantId());

        List<WebhookSubscription> subscriptions = webhookSubscriptionService.findCompatibleWebhookSubscriptionByStatus(merchant, message.status());

        if (subscriptions.isEmpty()){
            log.info("No webhook subscriptions for merchant {}", message.merchantId());
        }

        for (WebhookSubscription subscription : subscriptions) {
            WebhookPayload wp = payloadMapper.messageToWebhookPayload(message);

            WebhookEvent event = WebhookEvent.builder()
                            .type(webhookSubscriptionService.statusToEventType(message.status()))
                             .subscription(subscription)
                             .attempts(0)
                             .delivered(false)
                             .payload(objectMapper.writeValueAsString(wp))
                             .charge(chargeService.findById(message.chargeId()))
                             .build();

            dispatchWebhook(event);
        }

    }

    @Scheduled(fixedRate = 60000)
    public void retryFailedWebhooks(){
        List<WebhookEvent> events = webhookEventService.findUndelivered();
        log.info("Retrying failed webhooks for {} events", events.size());
        for (WebhookEvent event : events) {
            dispatchWebhook(event);
        }
    }

    private void dispatchWebhook(WebhookEvent event) {
        try {
            WebhookSubscription subscription = event.getSubscription();
            String signature = secretService.sign(event.getPayload(), secretService.decryptSecret(subscription.getSecretEncrypted()));

            ResponseEntity<Void> response = restClient.post()
                    .uri(subscription.getUrl())
                    .header("X-Signature", signature)
                    .body(event.getPayload())
                    .retrieve()
                    .toBodilessEntity();

            if (response.getStatusCode().is2xxSuccessful()) {
                event.setDelivered(true);
                log.info("Webhook delivered to {} for charge {}", subscription.getUrl(), event.getCharge().getId());
            }
        }
        catch (Exception e) {
            log.warn("Webhook delivery failed to {} (attempt {}): {}",
                    event.getSubscription().getUrl(), event.getAttempts() + 1, e.getMessage());
        }
        finally {
            event.setAttempts(event.getAttempts() + 1);
            event.setLastAttemptAt(LocalDateTime.now());
            webhookEventService.save(event);
        }

    }
}

