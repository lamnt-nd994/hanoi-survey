package com.hanoisurvey.api.interfaces.rest.equipments.dto;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipmentUpsertRequest(
        @NotNull Long categoryId,
        @NotBlank String name,
        @NotBlank String slug,
        String model,
        String manufacturer,
        String origin,
        String unit,
        Integer quantity,
        Integer productionYear,
        String description,
        String coverImagePath,
        @NotNull ContentStatus status,
        Integer sortOrder
) {
}
