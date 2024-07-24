package com.solbeg.nnewsservice.service;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class AuthUtil {
    public static String extractClaimStringValue(final Authentication authentication, final String claimName) {

        if (authentication != null && authentication.getDetails() instanceof Map) {
            final Object value = ((Map<?, ?>) authentication.getDetails()).get(claimName);
            if (value != null) {
                return value.toString();
            }
        }
        throw new RuntimeException("No claim with name " + claimName);
    }

    public static Set<SimpleGrantedAuthority> extractClaimAuthorities(final Authentication authentication) {
        if (authentication != null && authentication.getDetails() instanceof Map) {
            final var value = ((Map<?, ?>) authentication.getDetails()).get("authorities");
            if (value instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) value;
                return (authorities).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            }
        }
        throw new RuntimeException("No claim with name " + "authorities");
    }
}
