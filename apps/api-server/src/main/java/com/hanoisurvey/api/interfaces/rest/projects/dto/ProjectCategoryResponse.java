package com.hanoisurvey.api.interfaces.rest.projects.dto;

import com.hanoisurvey.api.domain.projects.ProjectCategory;

public record ProjectCategoryResponse(Long id, String name, String slug, Long parentId, Integer sortOrder, Boolean active) {
    public static ProjectCategoryResponse from(ProjectCategory category) {
        return new ProjectCategoryResponse(
                category.id(),
                category.name(),
                category.slug(),
                category.parentId(),
                category.sortOrder(),
                category.active()
        );
    }
}
