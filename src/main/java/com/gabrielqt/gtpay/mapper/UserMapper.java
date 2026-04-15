package com.gabrielqt.gtpay.mapper;

import com.gabrielqt.gtpay.dto.request.RegisterRequest;
import com.gabrielqt.gtpay.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;       // BCrypt que configuramos no SecurityConfig

    public User toEntity(RegisterRequest registerRequest) {
        return User.builder()
                .name(registerRequest.name())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .cpf(registerRequest.cpf())
                .role(registerRequest.role())
                .balance(BigDecimal.ZERO)
                .build();
    }
}
