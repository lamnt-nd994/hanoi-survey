package com.hanoisurvey.api.domain.auth;

import java.util.Set;

public record AuthenticatedUser(
        Long id,
        String username,
        String fullName,
        String email,
        Set<String> roles
) {
}
