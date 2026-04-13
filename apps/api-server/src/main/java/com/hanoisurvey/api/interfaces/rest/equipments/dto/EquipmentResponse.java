package com.hanoisurvey.api.interfaces.rest.equipments.dto;

import com.hanoisurvey.api.domain.equipments.Equipment;
import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;

public record EquipmentResponse(
        Long id,
        Long categoryId,
        String categoryName,
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
        Integer sortOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static EquipmentResponse from(Equipment equipment) {
        return new EquipmentResponse(
                equipment.id(),
                equipment.categoryId(),
                equipment.categoryName(),
                equipment.name(),
                equipment.slug(),
                equipment.model(),
                equipment.manufacturer(),
                equipment.origin(),
                equipment.unit(),
                equipment.quantity(),
                equipment.productionYear(),
                equipment.description(),
                equipment.coverImagePath(),
                equipment.status(),
                equipment.sortOrder(),
                equipment.createdAt(),
                equipment.updatedAt()
        );
    }
}
