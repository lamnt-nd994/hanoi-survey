package com.hanoisurvey.api.application.projects;

import com.hanoisurvey.api.domain.projects.Project;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProjectRepositoryPort {
    Optional<Project> findPublishedBySlug(String slug);

    org.springframework.data.domain.Page<Project> findPublished(String categorySlug, Pageable pageable);

    org.springframework.data.domain.Page<Project> findAll(Pageable pageable);

    Optional<Project> findById(Long id);

    boolean existsById(Long id);

    Project save(Project project);

    void deleteById(Long id);
}
