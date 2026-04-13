package com.hanoisurvey.api.domain.services;

import java.time.LocalDateTime;

public record ServiceImage(
        Long id,
        Long serviceId,
        String imagePath,
        String altText,
        String caption,
        Integer sortOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
