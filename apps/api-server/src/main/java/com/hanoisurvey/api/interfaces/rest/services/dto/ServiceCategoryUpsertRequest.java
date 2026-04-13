package com.hanoisurvey.api.interfaces.rest.services.dto;

import jakarta.validation.constraints.NotBlank;

public record ServiceCategoryUpsertRequest(
        @NotBlank String name,
        @NotBlank String slug,
        Long parentId,
        Integer sortOrder,
        Boolean active
) {
}
