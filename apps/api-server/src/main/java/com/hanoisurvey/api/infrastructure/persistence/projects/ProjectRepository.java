package com.hanoisurvey.api.infrastructure.persistence.projects;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    @EntityGraph(attributePaths = "category")
    Optional<ProjectEntity> findBySlugAndStatus(String slug, ContentStatus status);

    @EntityGraph(attributePaths = "category")
    Page<ProjectEntity> findByStatus(ContentStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<ProjectEntity> findByStatusAndCategory_Slug(ContentStatus status, String categorySlug, Pageable pageable);
}
