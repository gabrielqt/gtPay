package com.gabrielqt.gtpay.security.filter;

import com.gabrielqt.gtpay.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    // OncePerRequestFilter garante que esse filtro executa UMA vez por requisição

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Lista de rotas que NÃO precisam de token
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/login",
            "/auth/register"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Spring chama esse met. ANTES do doFilterInternal
        // se retornar true, o filtro inteiro é pulado para essa requisição
        String path = request.getServletPath();

        return PUBLIC_PATHS.stream()
                .anyMatch(path::equals); // verifica se o path atual está na lista de públicos
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // pega o header "Authorization" da requisição
        // ele vem assim: "Bearer eyJhbGci..."
        String authHeader = request.getHeader("Authorization");

        // esse check é só uma segurança extra — shouldNotFilter já filtrou as rotas públicas
        // mas ainda protege contra requisições sem token em rotas não mapeadas
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // remove o "Bearer " e fica só com o token
        String token = authHeader.substring(7);

        // extrai o email do token
        String username = jwtService.extractUsername(token);

        // se tem email e o usuário ainda não está autenticado nessa requisição
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // busca o usuário no banco pelo email
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // valida se o token é válido pra esse usuário
            if (jwtService.isTokenValid(token, userDetails)) {

                // cria o objeto de autenticação com o usuário e suas roles
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,                    // usuário
                                null,                           // credenciais (null pq já validamos)
                                userDetails.getAuthorities()    // roles
                        );

                // adiciona detalhes da requisição (IP, session, etc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // registra o usuário como autenticado no contexto do Spring Security
                // a partir daqui o Spring sabe quem está fazendo a requisição
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // passa pra próxima etapa da cadeia de filtros
        filterChain.doFilter(request, response);
    }}