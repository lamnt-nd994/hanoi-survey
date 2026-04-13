package com.hanoisurvey.api.domain.posts;

import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;

public record Post(
        Long id,
        Long categoryId,
        String categoryName,
        String title,
        String slug,
        String excerpt,
        String content,
        String coverImagePath,
        ContentStatus status,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
