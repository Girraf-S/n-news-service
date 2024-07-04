package com.solbeg.nnewsservice.config;

import com.solbeg.nnewsservice.exception.HeaderException;
import com.solbeg.nnewsservice.model.UserDetailsImpl;
import com.solbeg.nnewsservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RestTemplate restTemplate;

    private void setAuthenticationIfTokenValid(HttpServletRequest request, String username, String jwt) {
        UserDetails user = restTemplate.exchange("http://localhost:8081/auth/users", HttpMethod.POST,
                new HttpEntity<>(new HttpHeaders() {{
                    set(HttpHeaders.AUTHORIZATION,"Bearer "+ jwt);
                }}), UserDetailsImpl.class, username).getBody();

        Objects.requireNonNull(user);
        if (jwtService.isTokenValid(jwt, user.getUsername())) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new HeaderException("Header should be started with 'Bearer'");
        }
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        Objects.requireNonNull(username);
        setAuthenticationIfTokenValid(request, username, jwt);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        filterChain.doFilter(request, response);
    }
}
