package com.gabrielqt.gtpay.dto.request;

import com.gabrielqt.gtpay.entity.enums.PaymentType;
import com.gabrielqt.gtpay.entity.enums.Status;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ChargeRequest(
        @NotNull@Min(0)BigDecimal amount,
        String externalId,
        PaymentType paymentType
) {
}
