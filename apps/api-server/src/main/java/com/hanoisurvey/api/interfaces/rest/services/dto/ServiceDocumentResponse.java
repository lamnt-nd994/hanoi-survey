package com.hanoisurvey.api.interfaces.rest.services.dto;

import com.hanoisurvey.api.domain.services.ServiceDocument;

import java.time.LocalDateTime;

public record ServiceDocumentResponse(
        Long id,
        String title,
        String filePath,
        Integer sortOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ServiceDocumentResponse from(ServiceDocument document) {
        return new ServiceDocumentResponse(
                document.id(),
                document.title(),
                document.filePath(),
                document.sortOrder(),
                document.createdAt(),
                document.updatedAt()
        );
    }
}
