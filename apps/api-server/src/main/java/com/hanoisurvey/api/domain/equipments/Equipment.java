package com.hanoisurvey.api.domain.equipments;

import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;

public record Equipment(
        Long id,
        Long categoryId,
        String categoryName,
        String name,
        String slug,
        String model,
        String manufacturer,
        String origin,
        String unit,
        Integer quantity,
        Integer productionYear,
        String description,
        String coverImagePath,
        ContentStatus status,
        Integer sortOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
