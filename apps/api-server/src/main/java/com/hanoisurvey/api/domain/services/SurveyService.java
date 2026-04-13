package com.hanoisurvey.api.domain.services;

import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;

public record SurveyService(
        Long id,
        Long categoryId,
        String categoryName,
        String title,
        String slug,
        String overview,
        String content,
        String icon,
        String coverImagePath,
        ContentStatus status,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
