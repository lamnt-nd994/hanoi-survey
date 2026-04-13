package com.hanoisurvey.api.infrastructure.persistence.pages;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PageRepository extends JpaRepository<PageEntity, Long> {
    Optional<PageEntity> findBySlugAndStatus(String slug, ContentStatus status);

    Optional<PageEntity> findBySlug(String slug);

    Page<PageEntity> findAllByStatus(ContentStatus status, Pageable pageable);
}
