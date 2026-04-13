package com.hanoisurvey.api.interfaces.rest.services.dto;

import jakarta.validation.constraints.NotBlank;

public record ServiceImageUpsertRequest(
        @NotBlank String imagePath,
        String altText,
        String caption,
        Integer sortOrder
) {
}
