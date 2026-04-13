package com.hanoisurvey.api.interfaces.rest.services.dto;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SurveyServiceUpsertRequest(
        Long categoryId,
        @NotBlank String title,
        @NotBlank String slug,
        String overview,
        String content,
        String icon,
        String coverImagePath,
        String galleryJson,
        @Valid List<ServiceDocumentUpsertRequest> documents,
        @Valid List<ServiceImageUpsertRequest> images,
        @NotNull ContentStatus status
) {
}
