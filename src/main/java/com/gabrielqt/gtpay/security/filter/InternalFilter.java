package com.gabrielqt.gtpay.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class InternalFilter extends OncePerRequestFilter {

    @Value("${internal.token}")
    private String INTERNAL_TOKEN;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getRequestURI().startsWith("/internal")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!request.getHeader("X-Internal-Token").equals(INTERNAL_TOKEN)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid internal token\"}");
            return;
        }

        var auth = new UsernamePasswordAuthenticationToken(
                "INTERNAL", // principal: "quem é"
                null, // credentials: senha, etc.
                List.of(new SimpleGrantedAuthority("ROLE_INTERNAL")) // authorities: permissões
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
