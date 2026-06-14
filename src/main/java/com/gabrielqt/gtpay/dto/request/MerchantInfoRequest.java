package com.gabrielqt.gtpay.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MerchantInfoRequest(
        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "CEP must contain 8 digits without dash")
        String cep,

        @NotBlank
        @Size(max = 25, message = "Store name must have at most 25 characters")
        String storeName,

        @NotBlank
        String pixKey
) {
}
