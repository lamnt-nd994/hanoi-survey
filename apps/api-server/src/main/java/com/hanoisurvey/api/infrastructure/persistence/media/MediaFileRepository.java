package com.hanoisurvey.api.infrastructure.persistence.media;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaFileRepository extends JpaRepository<MediaFileEntity, Long> {
}
