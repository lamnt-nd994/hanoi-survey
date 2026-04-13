package com.hanoisurvey.api.infrastructure.persistence.projects;

import com.hanoisurvey.api.application.projects.ProjectRepositoryPort;
import com.hanoisurvey.api.domain.projects.Project;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProjectRepositoryAdapter implements ProjectRepositoryPort {

    private final ProjectRepository repository;
    private final ProjectCategoryRepository categoryRepository;

    public ProjectRepositoryAdapter(ProjectRepository repository, ProjectCategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Project> findPublishedBySlug(String slug) {
        return repository.findBySlugAndStatus(slug, ContentStatus.PUBLISHED).map(this::toDomain);
    }

    @Override
    public org.springframework.data.domain.Page<Project> findPublished(String categorySlug, Pageable pageable) {
        if (categorySlug == null || categorySlug.isBlank()) {
            return repository.findByStatus(ContentStatus.PUBLISHED, pageable).map(this::toDomain);
        }
        return repository.findByStatusAndCategory_Slug(ContentStatus.PUBLISHED, categorySlug, pageable).map(this::toDomain);
    }

    @Override
    public org.springframework.data.domain.Page<Project> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Project save(Project project) {
        ProjectEntity entity = project.id() == null ? new ProjectEntity() : repository.findById(project.id()).orElseGet(ProjectEntity::new);
        entity.setId(project.id());
        entity.setCategory(categoryRepository.findById(project.categoryId()).orElseThrow());
        entity.setTitle(project.title());
        entity.setSlug(project.slug());
        entity.setClientName(project.clientName());
        entity.setLocation(project.location());
        entity.setStartedAt(project.startedAt());
        entity.setCompletedAt(project.completedAt());
        entity.setScaleText(project.scaleText());
        entity.setCoverImagePath(project.coverImagePath());
        entity.setGalleryJson(project.galleryJson());
        entity.setContent(project.content());
        entity.setStatus(project.status());
        entity.setPublishedAt(project.publishedAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Project toDomain(ProjectEntity entity) {
        return new Project(entity.getId(), entity.getCategory().getId(), entity.getCategory().getName(), entity.getTitle(), entity.getSlug(), entity.getClientName(), entity.getLocation(), entity.getStartedAt(), entity.getCompletedAt(), entity.getScaleText(), entity.getCoverImagePath(), entity.getGalleryJson(), entity.getContent(), entity.getStatus(), entity.getPublishedAt(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
