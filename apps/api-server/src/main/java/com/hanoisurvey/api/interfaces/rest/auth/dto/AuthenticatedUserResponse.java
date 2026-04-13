package com.hanoisurvey.api.interfaces.rest.auth.dto;

import com.hanoisurvey.api.domain.auth.AuthenticatedUser;

import java.util.Set;

public record AuthenticatedUserResponse(Long id, String username, String fullName, String email, Set<String> roles) {
    public static AuthenticatedUserResponse from(AuthenticatedUser user) {
        return new AuthenticatedUserResponse(user.id(), user.username(), user.fullName(), user.email(), user.roles());
    }
}
