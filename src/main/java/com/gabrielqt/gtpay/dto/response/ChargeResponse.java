package com.gabrielqt.gtpay.dto.response;

import com.gabrielqt.gtpay.entity.enums.PaymentType;
import com.gabrielqt.gtpay.entity.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ChargeResponse(
        Long id,
        Status status,
        PaymentType paymentType,
        BigDecimal amount,
        String brCode,
        String externalId,
        LocalDateTime createdAt,
        LocalDateTime expiresAt
) {
}
