package com.hanoisurvey.api.application.media;

import com.hanoisurvey.api.domain.media.MediaFile;

import java.util.List;
import java.util.Optional;

public interface MediaFileRepositoryPort {
    List<MediaFile> findAll();

    MediaFile save(MediaFile mediaFile);

    Optional<MediaFile> findById(Long id);

    void deleteById(Long id);
}
