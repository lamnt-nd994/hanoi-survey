package com.hanoisurvey.api.application.services;

public record ServiceDocumentCommand(
        String title,
        String filePath,
        Integer sortOrder
) {
}
