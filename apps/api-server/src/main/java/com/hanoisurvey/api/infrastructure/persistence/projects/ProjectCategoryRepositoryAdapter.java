package com.hanoisurvey.api.infrastructure.persistence.projects;

import com.hanoisurvey.api.application.projects.ProjectCategoryRepositoryPort;
import com.hanoisurvey.api.domain.projects.ProjectCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProjectCategoryRepositoryAdapter implements ProjectCategoryRepositoryPort {

    private final ProjectCategoryRepository repository;

    public ProjectCategoryRepositoryAdapter(ProjectCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProjectCategory> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<ProjectCategory> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public ProjectCategory save(ProjectCategory category) {
        ProjectCategoryEntity entity = category.id() == null ? new ProjectCategoryEntity() : repository.findById(category.id()).orElseGet(ProjectCategoryEntity::new);
        entity.setId(category.id());
        entity.setName(category.name());
        entity.setSlug(category.slug());
        entity.setParent(category.parentId() == null ? null : repository.findById(category.parentId()).orElseThrow());
        entity.setSortOrder(category.sortOrder());
        entity.setActive(category.active());
        return toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private ProjectCategory toDomain(ProjectCategoryEntity entity) {
        return new ProjectCategory(entity.getId(), entity.getName(), entity.getSlug(), entity.getParent() == null ? null : entity.getParent().getId(), entity.getSortOrder(), entity.getActive());
    }
}
