package com.gabrielqt.gtpay.dto.response;

public record MerchantInfoResponse(
        String cep,
        String city,
        String storeName,
        String pixKey
) {
}
