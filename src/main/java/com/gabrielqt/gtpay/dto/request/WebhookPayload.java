package com.gabrielqt.gtpay.dto.request;

import com.gabrielqt.gtpay.entity.enums.Status;

import java.time.LocalDateTime;

public record WebhookPayload(
        String externalId,
        Status status,
        LocalDateTime confirmedAt
) {
}
