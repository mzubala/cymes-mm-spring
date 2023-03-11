package pl.com.bottega.cymes.commons.application;

import org.springframework.security.core.Authentication;

public class AuthenticationUtils {
    public static Long getUserId(Authentication authentication) {
        if(authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return (Long) authentication.getPrincipal();
    }
}
