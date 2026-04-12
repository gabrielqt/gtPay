package com.gabrielqt.gtpay.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // chave secreta usada pra assinar o token — em produção vai no application.propertie
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // 24 horas em milissegundos
    @Value("${jwt.expiration}")
    private long EXPIRATION;

    // gera o token com o email do usuário como subject
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())  // email do usuário
                .setIssuedAt(new Date())                // quando foi gerado
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // quando expira
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // assina com a secret key
                .compact(); // converte pra String
    }

    // extrai o email do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // valida se o token pertence ao usuário e não está expirado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // verifica se a data de expiração já passou
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // método genérico pra extrair qualquer informação do token
    // Claims é o "corpo" do token — contém subject, expiration, etc.
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token) // valida a assinatura e parseia
                .getBody();            // pega o corpo (Claims)
        return claimsResolver.apply(claims); // extrai o que foi pedido
    }

    // converte a SECRET_KEY string pra um objeto Key que o jjwt entende
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}