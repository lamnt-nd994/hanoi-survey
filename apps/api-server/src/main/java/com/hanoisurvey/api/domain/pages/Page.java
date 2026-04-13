package com.hanoisurvey.api.domain.pages;

import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;

public record Page(
        Long id,
        String title,
        String slug,
        String summary,
        String content,
        String templateCode,
        String contentJson,
        ContentStatus status,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
