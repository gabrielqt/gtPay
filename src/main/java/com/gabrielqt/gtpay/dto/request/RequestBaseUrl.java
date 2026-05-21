package com.gabrielqt.gtpay.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RequestBaseUrl(
        @NotBlank(message = "Base url is required.")
        String baseUrl
) {
}
