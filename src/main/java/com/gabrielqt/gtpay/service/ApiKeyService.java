package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.dto.response.ApiKeyResponse;
import com.gabrielqt.gtpay.entity.ApiKey;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.exception.ObjectNotFoundException;
import com.gabrielqt.gtpay.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final MerchantService merchantService;

    public Merchant findMerchantByApiKey(String rawKey) {
        String hashed = hashKey(rawKey);
        ApiKey apiKey = apiKeyRepository.findMerchantByKeyHash(hashed)
                .orElseThrow(() -> new ObjectNotFoundException("Merchant not found with this API key"));
        return apiKey.getMerchant();
    }

    public ApiKeyResponse generateApiKey(User user) {
        Merchant merchant = merchantService.findByUser(user);
        String rawKey = UUID.randomUUID().toString().replace("-", "");
        String hashed = hashKey(rawKey);

        ApiKey apiKey = ApiKey.builder().merchant(merchant).keyHash(hashed).build();
        apiKeyRepository.save(apiKey);

        return new ApiKeyResponse(rawKey);
    }

    public String hashKey(String rawKey) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(rawKey.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

