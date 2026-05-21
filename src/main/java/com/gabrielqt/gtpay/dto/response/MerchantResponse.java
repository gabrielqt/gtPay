package com.gabrielqt.gtpay.dto.response;


import com.gabrielqt.gtpay.entity.MerchantApiKey;
import com.gabrielqt.gtpay.entity.User;

import java.util.List;

public record MerchantResponse (
        Long id,
        String baseUrl,
        List<MerchantApiKey> apiKeys,
        User user
){
}
