package com.hanoisurvey.api.domain.users;

import java.time.LocalDateTime;

public record AdminRole(
        Long id,
        String code,
        String name,
        Boolean adminAccess,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
