package com.gabrielqt.gtpay.dto.request;

import com.gabrielqt.gtpay.entity.enums.CardBrand;
import com.gabrielqt.gtpay.entity.enums.PaymentResult;
import com.gabrielqt.gtpay.entity.enums.PaymentType;
import jakarta.validation.constraints.NotNull;

public record SimulatePaymentRequest
        (
                @NotNull Long chargeId,  // internal id gtpay
                @NotNull PaymentResult result,
                // if the payment type is card:
                CardBrand cardBrand,
                Integer cardLastDigits,
                String cardHolder
        ){
}
