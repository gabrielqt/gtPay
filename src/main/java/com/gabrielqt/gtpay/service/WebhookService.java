package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.config.RabbitMQConfig;
import com.gabrielqt.gtpay.dto.message.MessageCharge;
import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.WebhookEvent;
import com.gabrielqt.gtpay.entity.WebhookSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebhookService {
    private final RestClient restClient;
    private final MerchantService merchantService;
    private final WebhookSubscriptionService webhookSubscriptionService;
    private final WebhookEventService webhookEventService;

    @RabbitListener(queues = RabbitMQConfig.WEBHOOK_QUEUE)
    public void onPaymentConfirmed(MessageCharge message) {
        Merchant merchant = merchantService.findMerchantById(message.merchantId());
        List<WebhookSubscription> subscriptions = webhookSubscriptionService.findCompatibleWebhookSubscriptionByStatus(merchant, message.status());
        for (WebhookSubscription subscription : subscriptions) {
            dispatchWebhook(subscription);
        }

    }

    @Scheduled(fixedRate = 60000)
    public void retryFailedWebhooks(){
        List<WebhookEvent> events = webhookEventService.findUndelivered();
    }

    private void dispatchWebhook(WebhookSubscription subscription) {

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