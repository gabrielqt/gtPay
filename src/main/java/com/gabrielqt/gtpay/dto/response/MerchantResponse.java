package com.gabrielqt.gtpay.dto.response;


import com.gabrielqt.gtpay.entity.User;

import java.util.List;

public record MerchantResponse (
        Long id,
        String baseUrl,
        String cep,
        String city,
        String storeName,
        String pixKey,
        String user
){
}
