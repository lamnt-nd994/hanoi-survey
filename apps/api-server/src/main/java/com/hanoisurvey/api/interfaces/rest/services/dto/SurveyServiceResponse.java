package com.hanoisurvey.api.interfaces.rest.services.dto;

import com.hanoisurvey.api.domain.services.SurveyService;
import com.hanoisurvey.api.domain.shared.ContentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record SurveyServiceResponse(
        Long id,
        Long categoryId,
        String categoryName,
        String title,
        String slug,
        String overview,
        String content,
        String icon,
        String coverImagePath,
        String galleryJson,
        List<ServiceDocumentResponse> documents,
        List<ServiceImageResponse> images,
        ContentStatus status,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SurveyServiceResponse from(SurveyService service) {
        return new SurveyServiceResponse(
                service.id(),
                service.categoryId(),
                service.categoryName(),
                service.title(),
                service.slug(),
                service.overview(),
                service.content(),
                service.icon(),
                service.coverImagePath(),
                service.galleryJson(),
                List.of(),
                List.of(),
                service.status(),
                service.publishedAt(),
                service.createdAt(),
                service.updatedAt()
        );
    }

    public static SurveyServiceResponse from(SurveyService service, List<ServiceDocumentResponse> documents, List<ServiceImageResponse> images) {
        return new SurveyServiceResponse(
                service.id(),
                service.categoryId(),
                service.categoryName(),
                service.title(),
                service.slug(),
                service.overview(),
                service.content(),
                service.icon(),
                service.coverImagePath(),
                service.galleryJson(),
                documents == null ? List.of() : documents,
                images == null ? List.of() : images,
                service.status(),
                service.publishedAt(),
                service.createdAt(),
                service.updatedAt()
        );
    }
}
