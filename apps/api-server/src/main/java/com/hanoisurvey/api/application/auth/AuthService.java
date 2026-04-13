package com.hanoisurvey.api.application.auth;

import com.hanoisurvey.api.domain.auth.AuthenticatedUser;
import com.hanoisurvey.api.infrastructure.security.JwtTokenService;
import com.hanoisurvey.api.infrastructure.security.SecurityUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public AuthResult login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityUserDetails principal = (SecurityUserDetails) authentication.getPrincipal();
        return new AuthResult(jwtTokenService.generateToken(principal), principal.toAuthenticatedUser());
    }

    public AuthenticatedUser me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUserDetails principal)) {
            return null;
        }
        return principal.toAuthenticatedUser();
    }

    public record AuthResult(String accessToken, AuthenticatedUser user) {
    }
}
