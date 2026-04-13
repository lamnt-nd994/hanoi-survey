package com.hanoisurvey.api.application.posts;

public record PostCategoryCommand(String name, String slug, Long parentId, Integer sortOrder, Boolean active) {
}
