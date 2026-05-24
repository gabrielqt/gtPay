package com.gabrielqt.gtpay.dto.response;

import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.enums.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WebhookSubscriptionResponse(
        Long merchantId,
        String path,
        EventType event,
        String secret,
        boolean isActive
) {
}
