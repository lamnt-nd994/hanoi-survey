package com.hanoisurvey.api.interfaces.rest.users.dto;

import com.hanoisurvey.api.domain.users.AdminRole;

import java.time.LocalDateTime;

public record AdminRoleResponse(
        Long id,
        String code,
        String name,
        Boolean adminAccess,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AdminRoleResponse from(AdminRole role) {
        return new AdminRoleResponse(role.id(), role.code(), role.name(), role.adminAccess(), role.createdAt(), role.updatedAt());
    }
}
