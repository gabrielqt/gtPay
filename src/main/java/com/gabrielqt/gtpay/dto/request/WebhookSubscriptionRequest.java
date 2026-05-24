package com.gabrielqt.gtpay.dto.request;

import com.gabrielqt.gtpay.entity.enums.EventType;
import jakarta.validation.constraints.NotBlank;

public record WebhookSubscriptionRequest(
        @NotBlank String path,
        EventType event
) {
}
