package com.hanoisurvey.api.interfaces.rest.services.dto;

import jakarta.validation.constraints.NotBlank;

public record ServiceDocumentUpsertRequest(
        @NotBlank String title,
        @NotBlank String filePath,
        Integer sortOrder
) {
}
