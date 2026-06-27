package com.gabrielqt.gtpay.dto.message;

import com.gabrielqt.gtpay.entity.enums.CardBrand;
import com.gabrielqt.gtpay.entity.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MessageCharge (
        Long chargeId,
        String externalId,
        Long merchantId,
        Status status,
        LocalDateTime confirmedAt
) {
}
