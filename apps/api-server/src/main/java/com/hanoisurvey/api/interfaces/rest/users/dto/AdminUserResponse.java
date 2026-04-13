package com.hanoisurvey.api.interfaces.rest.users.dto;

import com.hanoisurvey.api.domain.users.AdminUser;

import java.time.LocalDateTime;
import java.util.Set;

public record AdminUserResponse(
        Long id,
        String username,
        String fullName,
        String email,
        Boolean active,
        Set<AdminRoleResponse> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AdminUserResponse from(AdminUser user) {
        return new AdminUserResponse(
                user.id(),
                user.username(),
                user.fullName(),
                user.email(),
                user.active(),
                user.roles().stream().map(AdminRoleResponse::from).collect(java.util.stream.Collectors.toSet()),
                user.createdAt(),
                user.updatedAt()
        );
    }
}
