package com.gabrielqt.gtpay.dto.request;

import com.gabrielqt.gtpay.entity.enums.Status;

public record WebhookPayload(
        String externalId,
        Status status,
        String confirmedAt
) {
}
