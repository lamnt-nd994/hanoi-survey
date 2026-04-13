package com.hanoisurvey.api.interfaces.rest.equipments.dto;

import jakarta.validation.constraints.NotBlank;

public record EquipmentCategoryUpsertRequest(
        @NotBlank String name,
        @NotBlank String slug,
        Long parentId,
        String icon,
        Integer sortOrder,
        Boolean active
) {
}
