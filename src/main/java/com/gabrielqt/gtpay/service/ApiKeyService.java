package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.entity.ApiKey;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.exception.ObjectNotFoundException;
import com.gabrielqt.gtpay.repository.ApiKeyRepository;
import com.gabrielqt.gtpay.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;

    public Merchant findMerchantByApiKey(String apiKey) {
        return
                apiKeyRepository.findMerchantByApiKey(apiKey)
                        .orElseThrow(() -> new ObjectNotFoundException("Merchant not found with this api key."));
    }

}

