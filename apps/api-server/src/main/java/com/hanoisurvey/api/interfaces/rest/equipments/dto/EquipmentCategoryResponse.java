package com.hanoisurvey.api.interfaces.rest.equipments.dto;

import com.hanoisurvey.api.domain.equipments.EquipmentCategory;

public record EquipmentCategoryResponse(Long id, String name, String slug, Long parentId, String icon, Integer sortOrder, Boolean active) {
    public static EquipmentCategoryResponse from(EquipmentCategory category) {
        return new EquipmentCategoryResponse(
                category.id(),
                category.name(),
                category.slug(),
                category.parentId(),
                category.icon(),
                category.sortOrder(),
                category.active()
        );
    }
}
