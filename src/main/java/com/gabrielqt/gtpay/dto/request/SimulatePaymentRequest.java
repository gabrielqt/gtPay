package com.gabrielqt.gtpay.dto.request;

import com.gabrielqt.gtpay.entity.enums.PaymentResult;
import com.gabrielqt.gtpay.entity.enums.PaymentType;
import jakarta.validation.constraints.NotNull;

public record SimulatePaymentRequest
        (
                @NotNull String externalId,
                @NotNull PaymentType type,
                @NotNull PaymentResult result,
                String cardBrand,
                Integer cardLastDigits,
                String cardHolder
        ){
}
