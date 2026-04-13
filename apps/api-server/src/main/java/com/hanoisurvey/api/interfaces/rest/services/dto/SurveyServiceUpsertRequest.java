package com.hanoisurvey.api.interfaces.rest.services.dto;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SurveyServiceUpsertRequest(
        @NotNull Long categoryId,
        @NotBlank String title,
        @NotBlank String slug,
        String overview,
        String content,
        String icon,
        String coverImagePath,
        @NotNull ContentStatus status
) {
}
