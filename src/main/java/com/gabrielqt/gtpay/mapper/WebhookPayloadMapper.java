package com.gabrielqt.gtpay.mapper;


import com.gabrielqt.gtpay.dto.message.MessageCharge;
import com.gabrielqt.gtpay.dto.request.WebhookPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebhookPayloadMapper {
    public WebhookPayload messageToWebhookPayload(MessageCharge msg) {
        return new WebhookPayload(
                msg.externalId(),
                msg.status(),
                msg.confirmedAt()
        );
    }
}
