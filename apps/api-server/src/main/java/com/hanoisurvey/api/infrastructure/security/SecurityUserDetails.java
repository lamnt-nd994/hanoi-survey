package com.hanoisurvey.api.infrastructure.security;

import com.hanoisurvey.api.domain.auth.AuthenticatedUser;
import com.hanoisurvey.api.infrastructure.persistence.auth.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class SecurityUserDetails implements UserDetails {

    private final UserEntity user;

    public SecurityUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(toAuthority(role.getCode())))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getActive());
    }

    public AuthenticatedUser toAuthenticatedUser() {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getCode())
                .collect(java.util.stream.Collectors.toSet());
        return new AuthenticatedUser(user.getId(), user.getUsername(), user.getFullName(), user.getEmail(), roles);
    }

    public boolean hasAdminAccess() {
        return user.getRoles().stream().anyMatch(role -> Boolean.TRUE.equals(role.getAdminAccess()));
    }

    private String toAuthority(String roleCode) {
        return roleCode != null && roleCode.startsWith("ROLE_") ? roleCode : "ROLE_" + roleCode;
    }
}
