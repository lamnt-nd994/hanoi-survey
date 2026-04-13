package com.hanoisurvey.api.domain.users;

import java.time.LocalDateTime;
import java.util.Set;

public record AdminUser(
        Long id,
        String username,
        String fullName,
        String email,
        Boolean active,
        Set<AdminRole> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
