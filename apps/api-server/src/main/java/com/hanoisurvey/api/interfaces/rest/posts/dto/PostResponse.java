package com.hanoisurvey.api.interfaces.rest.posts.dto;

import com.hanoisurvey.api.domain.posts.Post;
import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long categoryId,
        String categoryName,
        String title,
        String slug,
        String excerpt,
        String content,
        String coverImagePath,
        ContentStatus status,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.id(),
                post.categoryId(),
                post.categoryName(),
                post.title(),
                post.slug(),
                post.excerpt(),
                post.content(),
                post.coverImagePath(),
                post.status(),
                post.publishedAt(),
                post.createdAt(),
                post.updatedAt()
        );
    }
}
