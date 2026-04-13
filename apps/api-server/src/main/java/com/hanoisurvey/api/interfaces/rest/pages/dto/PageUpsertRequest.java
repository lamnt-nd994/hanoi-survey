package com.hanoisurvey.api.interfaces.rest.pages.dto;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PageUpsertRequest(
        @NotBlank String title,
        @NotBlank String slug,
        String summary,
        String content,
        @NotBlank String templateCode,
        String contentJson,
        @NotNull ContentStatus status
) {
}
