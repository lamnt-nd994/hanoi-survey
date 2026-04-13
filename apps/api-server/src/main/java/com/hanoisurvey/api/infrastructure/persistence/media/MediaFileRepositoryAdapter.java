package com.hanoisurvey.api.infrastructure.persistence.media;

import com.hanoisurvey.api.application.media.MediaFileRepositoryPort;
import com.hanoisurvey.api.domain.media.MediaFile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MediaFileRepositoryAdapter implements MediaFileRepositoryPort {

    private final MediaFileRepository repository;

    public MediaFileRepositoryAdapter(MediaFileRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MediaFile> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public MediaFile save(MediaFile mediaFile) {
        MediaFileEntity entity = mediaFile.id() == null ? new MediaFileEntity() : repository.findById(mediaFile.id()).orElseGet(MediaFileEntity::new);
        entity.setId(mediaFile.id());
        entity.setFileName(mediaFile.fileName());
        entity.setMimeType(mediaFile.mimeType());
        entity.setSizeBytes(mediaFile.sizeBytes());
        entity.setStoragePath(mediaFile.storagePath());
        entity.setAltText(mediaFile.altText());
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<MediaFile> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private MediaFile toDomain(MediaFileEntity entity) {
        return new MediaFile(entity.getId(), entity.getFileName(), entity.getMimeType(), entity.getSizeBytes(), entity.getStoragePath(), entity.getAltText(), entity.getCreatedAt());
    }
}
