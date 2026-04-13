package com.hanoisurvey.api.application.services;

public record ServiceImageCommand(
        String imagePath,
        String altText,
        String caption,
        Integer sortOrder
) {
}
