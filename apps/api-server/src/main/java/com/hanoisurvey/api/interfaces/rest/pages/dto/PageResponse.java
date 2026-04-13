package com.hanoisurvey.api.interfaces.rest.pages.dto;

import com.hanoisurvey.api.domain.pages.Page;
import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;

public record PageResponse(
        Long id,
        String title,
        String slug,
        String summary,
        String content,
        String templateCode,
        String contentJson,
        ContentStatus status,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PageResponse from(Page page) {
        return new PageResponse(
                page.id(),
                page.title(),
                page.slug(),
                page.summary(),
                page.content(),
                page.templateCode(),
                page.contentJson(),
                page.status(),
                page.publishedAt(),
                page.createdAt(),
                page.updatedAt()
        );
    }
}
