package com.hanoisurvey.api.domain.posts;

public record PostCategory(Long id, String name, String slug, Long parentId, Integer sortOrder, Boolean active) {
}
