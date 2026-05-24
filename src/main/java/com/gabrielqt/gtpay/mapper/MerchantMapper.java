package com.gabrielqt.gtpay.mapper;

import com.gabrielqt.gtpay.dto.response.MerchantResponse;
import com.gabrielqt.gtpay.entity.Merchant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MerchantMapper {

    public MerchantResponse toMerchantResponse(Merchant merchant) {
        return new MerchantResponse(
                merchant.getId(),
                merchant.getBaseUrl(),
                merchant.getUser()
                );
    }
}
