package com.gabrielqt.gtpay.security.filter;

import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {
    private final ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!path.startsWith("/webhook/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("X-Api-Key");
        if (apiKey == null || apiKey.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Missing API Key\"}");
            return;
        }
        Merchant merchant = apiKeyService.findMerchantByApiKey(apiKey);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(merchant, null, merchant.getUser().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);

    }
}
