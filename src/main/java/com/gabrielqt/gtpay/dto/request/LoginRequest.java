package com.gabrielqt.gtpay.dto.request;

import com.gabrielqt.gtpay.validators.interfaces.Password;
import jakarta.validation.constraints.Email;

public record LoginRequest(
        @Email String email,
        @Password String password
) {
}
