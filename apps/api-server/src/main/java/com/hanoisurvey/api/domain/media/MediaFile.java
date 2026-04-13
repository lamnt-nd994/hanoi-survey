package com.hanoisurvey.api.domain.media;

import java.time.LocalDateTime;

public record MediaFile(
        Long id,
        String fileName,
        String mimeType,
        Long sizeBytes,
        String storagePath,
        String altText,
        LocalDateTime createdAt
) {
}
