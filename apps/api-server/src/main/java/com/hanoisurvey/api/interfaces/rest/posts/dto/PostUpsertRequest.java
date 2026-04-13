package com.hanoisurvey.api.interfaces.rest.posts.dto;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostUpsertRequest(
        @NotNull Long categoryId,
        @NotBlank String title,
        @NotBlank String slug,
        String excerpt,
        String content,
        String coverImagePath,
        @NotNull ContentStatus status
) {
}
