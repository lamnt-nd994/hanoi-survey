package com.hanoisurvey.api.infrastructure.persistence.services;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyServiceRepository extends JpaRepository<SurveyServiceEntity, Long> {
    @EntityGraph(attributePaths = "category")
    Optional<SurveyServiceEntity> findBySlugAndStatus(String slug, ContentStatus status);

    @EntityGraph(attributePaths = "category")
    Page<SurveyServiceEntity> findByStatus(ContentStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<SurveyServiceEntity> findByStatusAndCategory_Slug(ContentStatus status, String categorySlug, Pageable pageable);
}
