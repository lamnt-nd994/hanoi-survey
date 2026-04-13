package com.hanoisurvey.api.application.equipments;

public record EquipmentCategoryCommand(String name, String slug, Long parentId, String icon, Integer sortOrder, Boolean active) {
}
