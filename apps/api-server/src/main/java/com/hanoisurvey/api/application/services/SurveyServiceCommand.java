package com.hanoisurvey.api.application.services;

import com.hanoisurvey.api.domain.shared.ContentStatus;

public record SurveyServiceCommand(
        Long categoryId,
        String title,
        String slug,
        String overview,
        String content,
        String icon,
        String coverImagePath,
        String galleryJson,
        java.util.List<ServiceDocumentCommand> documents,
        java.util.List<ServiceImageCommand> images,
        ContentStatus status
) {
}
