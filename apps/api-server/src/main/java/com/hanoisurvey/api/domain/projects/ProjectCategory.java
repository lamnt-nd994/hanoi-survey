package com.hanoisurvey.api.domain.projects;

public record ProjectCategory(Long id, String name, String slug, Long parentId, Integer sortOrder, Boolean active) {
}
