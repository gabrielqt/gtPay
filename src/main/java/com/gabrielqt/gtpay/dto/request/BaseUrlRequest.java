package com.gabrielqt.gtpay.dto.request;

import com.gabrielqt.gtpay.validators.interfaces.BaseUrl;
import jakarta.validation.constraints.NotBlank;

public record BaseUrlRequest(
        @BaseUrl String baseUrl
) {
}
