package com.hanoisurvey.api.domain.services;

import java.time.LocalDateTime;

public record ServiceDocument(
        Long id,
        Long serviceId,
        String title,
        String filePath,
        Integer sortOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
