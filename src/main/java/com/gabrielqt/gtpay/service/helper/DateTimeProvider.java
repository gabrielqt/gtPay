package com.gabrielqt.gtpay.service.helper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateTimeProvider {
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    public LocalDateTime expiresInMinutes(Integer minutes) {
        return now().plusMinutes(minutes);
    }
}
