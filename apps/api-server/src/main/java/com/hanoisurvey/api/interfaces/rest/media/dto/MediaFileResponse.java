package com.hanoisurvey.api.interfaces.rest.media.dto;

import com.hanoisurvey.api.domain.media.MediaFile;

import java.time.LocalDateTime;

public record MediaFileResponse(
        Long id,
        String fileName,
        String mimeType,
        Long sizeBytes,
        String storagePath,
        String altText,
        LocalDateTime createdAt
) {
    public static MediaFileResponse from(MediaFile mediaFile) {
        return new MediaFileResponse(
                mediaFile.id(),
                mediaFile.fileName(),
                mediaFile.mimeType(),
                mediaFile.sizeBytes(),
                mediaFile.storagePath(),
                mediaFile.altText(),
                mediaFile.createdAt()
        );
    }
}
