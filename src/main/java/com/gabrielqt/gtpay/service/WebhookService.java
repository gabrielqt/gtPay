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
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

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
        Merchant merchant = merchantService.findMerchantById(message.merchantId());
        List<WebhookSubscription> subscriptions = webhookSubscriptionService.findCompatibleWebhookSubscriptionByStatus(merchant, message.status());

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
            }
        }
        catch (Exception e) {
//            loggar
        }
        finally {
            event.setAttempts(event.getAttempts() + 1);
            event.setLastAttemptAt(LocalDateTime.now());
            webhookEventService.save(event);
        }

    }
}


// um webhook event pra cada webhooksubscription

// carregar a lista de webhook events pra saber se é um retry ou não

// 1. busca a subscription do merchant pro evento
// 2. monta o payload JSON
// 3. calcula a assinatura HMAC
// 4. faz o POST (try-catch)
// 5. registra o WebhookEvent (delivered conforme resultado)
//


//restClient.post()
//    .uri(...)           // a URL do merchant
//    .header(...)        // aqui vai a assinatura HMAC
//    .body(...)          // o payload
//    .retrieve()
//    .toBodilessEntity() // ou toEntity se quiser a resposta