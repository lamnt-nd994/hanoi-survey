package com.hanoisurvey.api.interfaces.rest.services.dto;

import com.hanoisurvey.api.domain.services.ServiceCategory;

public record ServiceCategoryResponse(Long id, String name, String slug, Long parentId, Integer sortOrder, Boolean active) {

    public static ServiceCategoryResponse from(ServiceCategory category) {
        return new ServiceCategoryResponse(
                category.id(),
                category.name(),
                category.slug(),
                category.parentId(),
                category.sortOrder(),
                category.active()
        );
    }
}
