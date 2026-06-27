package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.config.RabbitMQConfig;
import com.gabrielqt.gtpay.dto.message.MessageCharge;
import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.entity.Merchant;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class WebhookService {
    private final RestClient restClient;
    private final MerchantService merchantService;

    @RabbitListener(queues = RabbitMQConfig.WEBHOOK_QUEUE)
    public void onPaymentConfirmed(MessageCharge message) {
        Merchant merchant = merchantService.findMerchantById(message.merchantId());

    }

    private void dispatchWebhook(){

    }
}



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