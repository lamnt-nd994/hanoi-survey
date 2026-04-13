package com.hanoisurvey.api.application.projects;

public record ProjectCategoryCommand(String name, String slug, Long parentId, Integer sortOrder, Boolean active) {
}
