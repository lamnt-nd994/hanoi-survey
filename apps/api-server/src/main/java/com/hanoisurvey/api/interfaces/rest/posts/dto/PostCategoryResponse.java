package com.hanoisurvey.api.interfaces.rest.posts.dto;

import com.hanoisurvey.api.domain.posts.PostCategory;

public record PostCategoryResponse(Long id, String name, String slug, Long parentId, Integer sortOrder, Boolean active) {
    public static PostCategoryResponse from(PostCategory category) {
        return new PostCategoryResponse(
                category.id(),
                category.name(),
                category.slug(),
                category.parentId(),
                category.sortOrder(),
                category.active()
        );
    }
}
