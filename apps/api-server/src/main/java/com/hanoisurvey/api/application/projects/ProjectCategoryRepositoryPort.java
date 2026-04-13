package com.hanoisurvey.api.application.projects;

import com.hanoisurvey.api.domain.projects.ProjectCategory;

import java.util.List;
import java.util.Optional;

public interface ProjectCategoryRepositoryPort {
    List<ProjectCategory> findAll();

    Optional<ProjectCategory> findById(Long id);

    boolean existsById(Long id);

    ProjectCategory save(ProjectCategory category);

    void deleteById(Long id);
}
