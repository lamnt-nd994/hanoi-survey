package com.hanoisurvey.api.application.equipments;

import com.hanoisurvey.api.domain.shared.ContentStatus;

public record EquipmentCommand(
        Long categoryId,
        String name,
        String slug,
        String model,
        String manufacturer,
        String origin,
        String unit,
        Integer quantity,
        Integer productionYear,
        String description,
        String coverImagePath,
        ContentStatus status,
        Integer sortOrder
) {
}
