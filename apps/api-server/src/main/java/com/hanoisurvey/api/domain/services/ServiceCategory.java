package com.hanoisurvey.api.domain.services;

public record ServiceCategory(Long id, String name, String slug, Long parentId, Integer sortOrder, Boolean active) {
}
