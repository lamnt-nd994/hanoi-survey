package com.hanoisurvey.api.infrastructure.persistence.services;

import com.hanoisurvey.api.application.services.SurveyServiceRepositoryPort;
import com.hanoisurvey.api.domain.services.SurveyService;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SurveyServiceRepositoryAdapter implements SurveyServiceRepositoryPort {

    private final SurveyServiceRepository repository;
    private final ServiceCategoryRepository categoryRepository;

    public SurveyServiceRepositoryAdapter(SurveyServiceRepository repository, ServiceCategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<SurveyService> findPublishedBySlug(String slug) {
        return repository.findBySlugAndStatus(slug, ContentStatus.PUBLISHED).map(this::toDomain);
    }

    @Override
    public org.springframework.data.domain.Page<SurveyService> findPublished(String categorySlug, Pageable pageable) {
        if (categorySlug == null || categorySlug.isBlank()) {
            return repository.findByStatus(ContentStatus.PUBLISHED, pageable).map(this::toDomain);
        }
        return repository.findByStatusAndCategory_Slug(ContentStatus.PUBLISHED, categorySlug, pageable).map(this::toDomain);
    }

    @Override
    public org.springframework.data.domain.Page<SurveyService> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Optional<SurveyService> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public SurveyService save(SurveyService surveyService) {
        SurveyServiceEntity entity = surveyService.id() == null ? new SurveyServiceEntity() : repository.findById(surveyService.id()).orElseGet(SurveyServiceEntity::new);
        entity.setId(surveyService.id());
        if (surveyService.categoryId() == null || surveyService.categoryId() <= 0) {
            entity.setCategory(null);
        } else {
            entity.setCategory(categoryRepository.findById(surveyService.categoryId()).orElseThrow());
        }
        entity.setTitle(surveyService.title());
        entity.setSlug(surveyService.slug());
        entity.setOverview(surveyService.overview());
        entity.setContent(surveyService.content());
        entity.setIcon(surveyService.icon());
        entity.setCoverImagePath(surveyService.coverImagePath());
        entity.setGalleryJson(surveyService.galleryJson());
        entity.setStatus(surveyService.status());
        entity.setPublishedAt(surveyService.publishedAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private SurveyService toDomain(SurveyServiceEntity entity) {
        ServiceCategoryEntity category = entity.getCategory();
        return new SurveyService(entity.getId(), category == null ? null : category.getId(), category == null ? null : category.getName(), entity.getTitle(), entity.getSlug(), entity.getOverview(), entity.getContent(), entity.getIcon(), entity.getCoverImagePath(), entity.getGalleryJson(), entity.getStatus(), entity.getPublishedAt(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
