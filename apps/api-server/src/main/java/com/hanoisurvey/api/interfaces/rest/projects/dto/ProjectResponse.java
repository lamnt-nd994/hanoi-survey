package com.hanoisurvey.api.interfaces.rest.projects.dto;

import com.hanoisurvey.api.domain.projects.Project;
import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        Long categoryId,
        String categoryName,
        String title,
        String slug,
        String clientName,
        String location,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        String scaleText,
        String coverImagePath,
        String galleryJson,
        String content,
        ContentStatus status,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
                project.id(),
                project.categoryId(),
                project.categoryName(),
                project.title(),
                project.slug(),
                project.clientName(),
                project.location(),
                project.startedAt(),
                project.completedAt(),
                project.scaleText(),
                project.coverImagePath(),
                project.galleryJson(),
                project.content(),
                project.status(),
                project.publishedAt(),
                project.createdAt(),
                project.updatedAt()
        );
    }
}
