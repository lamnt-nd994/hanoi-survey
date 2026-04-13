package com.hanoisurvey.api.interfaces.rest.projects.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectCategoryUpsertRequest(
        @NotBlank String name,
        @NotBlank String slug,
        Long parentId,
        Integer sortOrder,
        Boolean active
) {
}
