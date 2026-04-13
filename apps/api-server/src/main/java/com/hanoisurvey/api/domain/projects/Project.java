package com.hanoisurvey.api.domain.projects;

import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;

public record Project(
        Long id,
        Long categoryId,
        String categoryName,
        String title,
        String slug,
        String clientName,
        String location,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        String scaleText,
        String coverImagePath,
        String galleryJson,
        String content,
        ContentStatus status,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
