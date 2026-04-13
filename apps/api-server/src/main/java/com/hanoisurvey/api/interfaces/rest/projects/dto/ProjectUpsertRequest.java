package com.hanoisurvey.api.interfaces.rest.projects.dto;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ProjectUpsertRequest(
        @NotNull Long categoryId,
        @NotBlank String title,
        @NotBlank String slug,
        String clientName,
        String location,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        String scaleText,
        String coverImagePath,
        String galleryJson,
        String content,
        @NotNull ContentStatus status
) {
}
