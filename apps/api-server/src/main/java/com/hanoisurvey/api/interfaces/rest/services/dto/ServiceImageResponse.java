package com.hanoisurvey.api.interfaces.rest.services.dto;

import com.hanoisurvey.api.domain.services.ServiceImage;

import java.time.LocalDateTime;

public record ServiceImageResponse(
        Long id,
        String imagePath,
        String altText,
        String caption,
        Integer sortOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ServiceImageResponse from(ServiceImage image) {
        return new ServiceImageResponse(
                image.id(),
                image.imagePath(),
                image.altText(),
                image.caption(),
                image.sortOrder(),
                image.createdAt(),
                image.updatedAt()
        );
    }
}
