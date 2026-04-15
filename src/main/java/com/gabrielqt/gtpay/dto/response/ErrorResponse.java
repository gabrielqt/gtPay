package com.gabrielqt.gtpay.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        Integer code,
        String message,
        LocalDateTime timestamp
) {
}
