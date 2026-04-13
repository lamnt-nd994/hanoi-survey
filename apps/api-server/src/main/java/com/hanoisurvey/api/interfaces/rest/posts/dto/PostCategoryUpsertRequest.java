package com.hanoisurvey.api.interfaces.rest.posts.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCategoryUpsertRequest(
        @NotBlank String name,
        @NotBlank String slug,
        Long parentId,
        Integer sortOrder,
        Boolean active
) {
}
