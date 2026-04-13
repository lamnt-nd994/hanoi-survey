package com.hanoisurvey.api.infrastructure.persistence.projects;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectCategoryRepository extends JpaRepository<ProjectCategoryEntity, Long> {
}
