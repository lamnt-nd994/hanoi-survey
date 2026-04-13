package com.hanoisurvey.api.application.services;

public record ServiceCategoryCommand(String name, String slug, Long parentId, Integer sortOrder, Boolean active) {
}
