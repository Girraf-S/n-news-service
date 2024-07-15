package com.solbeg.nnewsservice.config;

import com.solbeg.nnewsservice.exception.HeaderException;
import com.solbeg.nnewsservice.security.UserDetailsImpl;
import com.solbeg.nnewsservice.service.AuthUtil;
import com.solbeg.nnewsservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Value("${jwt.bearer}")
    private String bearer;
    @Value("${jwt.begin-index}")
    private int beginIndex;
    @Value("${service.user-domain}")
    private String userDomain;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;

        if (authHeader == null || !authHeader.startsWith(bearer)) {
            throw new HeaderException("Header should be started with 'Bearer'");
        }
        jwt = authHeader.substring(beginIndex);

        setAuthenticationIfTokenValid(request, jwt);
        response.setHeader(HttpHeaders.AUTHORIZATION, bearer + jwt);
        filterChain.doFilter(request, response);
    }

    private UserDetailsImpl findUserByToken(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, bearer + jwt);
        UserDetailsImpl user = restTemplate.exchange(userDomain + "/auth/users", HttpMethod.POST,
                new HttpEntity<>(headers), UserDetailsImpl.class).getBody();
        Objects.requireNonNull(user);
        user.setAuthorities(AuthUtil.extractClaimAuthorities(SecurityContextHolder.getContext().getAuthentication()));
        return user;
    }

    private void setAuthenticationIfTokenValid(HttpServletRequest request, String jwt) {
        UserDetails user = findUserByToken(jwt);
        if (jwtService.isTokenValid(jwt, user.getUsername())) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );
            authToken.setDetails(
                    jwtService.extractClaims(request.getHeader(HttpHeaders.AUTHORIZATION).substring(beginIndex))
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
}
