package fafenterprise.dev.gograduation.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import fafenterprise.dev.gograduation.entity.uno.UserEntity;
import fafenterprise.dev.gograduation.repository.UserRepository;
import fafenterprise.dev.gograduation.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtService.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID userId = jwtService.getUserIdFromToken(token);

        UserEntity user = userRepository
                .findById(userId)
                .orElse(null);

        if (user != null) {

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            java.util.Collections.emptyList());

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}