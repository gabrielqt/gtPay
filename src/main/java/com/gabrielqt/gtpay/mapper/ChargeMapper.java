package com.gabrielqt.gtpay.mapper;

import com.gabrielqt.gtpay.dto.response.ChargeResponse;
import com.gabrielqt.gtpay.entity.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargeMapper {
    public ChargeResponse toChargeResponse(Charge charge) {
        return new ChargeResponse(
                charge.getId(),
                charge.getStatus(),
                charge.getPaymentType(),
                charge.getAmount(),
                charge.getBrCode(),
                charge.getExternalId(),
                charge.getCreatedAt(),
                charge.getExpiresAt()
        );
    }
}
