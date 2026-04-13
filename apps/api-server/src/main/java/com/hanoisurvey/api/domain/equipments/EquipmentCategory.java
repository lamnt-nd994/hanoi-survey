package com.hanoisurvey.api.domain.equipments;

public record EquipmentCategory(Long id, String name, String slug, Long parentId, String icon, Integer sortOrder, Boolean active) {
}
