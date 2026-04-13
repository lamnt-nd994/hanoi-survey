package com.hanoisurvey.api.application.projects;

import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;

public record ProjectCommand(
        Long categoryId,
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
        ContentStatus status
) {
}
