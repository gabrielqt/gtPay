package com.gabrielqt.gtpay.service;
import com.gabrielqt.gtpay.dto.request.LoginRequest;
import com.gabrielqt.gtpay.dto.request.RegisterRequest;
import com.gabrielqt.gtpay.dto.response.TokenResponse;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.entity.enums.Role;
import com.gabrielqt.gtpay.exception.ObjectNotFoundException;
import com.gabrielqt.gtpay.mapper.UserMapper;
import com.gabrielqt.gtpay.repository.UserRepository;
import com.gabrielqt.gtpay.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; // gerencia o processo de autenticação
    private final UserMapper userMapper;

    public TokenResponse login(LoginRequest request) {

        // AuthenticationManager valida o email e senha automaticamente
        // se errado, lança exceção BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),    // username
                        request.password()  // senha em texto puro — Spring compara com o BCrypt do banco
                )
        );

        // se chegou aqui, credenciais estão corretas
        // busca o usuário no banco pelo email
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        // gera o token JWT pro usuário
        String token = jwtService.generateToken(user);

        // retorna o token pro cliente
        return new TokenResponse(token);
    }

    public void register(RegisterRequest request) {

        userRepository.save(userMapper.toEntity(request));
    }
}