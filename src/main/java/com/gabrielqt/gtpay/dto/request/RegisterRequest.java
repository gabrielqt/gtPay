package com.gabrielqt.gtpay.dto.request;

import com.gabrielqt.gtpay.entity.enums.Role;
import com.gabrielqt.gtpay.validators.interfaces.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record RegisterRequest(
    @NotNull String name,
    @Email String email,
    @Password() String password,
    @CPF String cpf,
    Role role,
    @NotNull BigDecimal balance
) {
}
