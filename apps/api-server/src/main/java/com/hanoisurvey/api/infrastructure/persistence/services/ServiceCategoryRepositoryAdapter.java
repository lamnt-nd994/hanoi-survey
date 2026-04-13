package com.hanoisurvey.api.infrastructure.persistence.services;

import com.hanoisurvey.api.application.services.ServiceCategoryRepositoryPort;
import com.hanoisurvey.api.domain.services.ServiceCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ServiceCategoryRepositoryAdapter implements ServiceCategoryRepositoryPort {

    private final ServiceCategoryRepository repository;

    public ServiceCategoryRepositoryAdapter(ServiceCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ServiceCategory> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<ServiceCategory> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public ServiceCategory save(ServiceCategory category) {
        ServiceCategoryEntity entity = category.id() == null ? new ServiceCategoryEntity() : repository.findById(category.id()).orElseGet(ServiceCategoryEntity::new);
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

    private ServiceCategory toDomain(ServiceCategoryEntity entity) {
        return new ServiceCategory(entity.getId(), entity.getName(), entity.getSlug(), entity.getParent() == null ? null : entity.getParent().getId(), entity.getSortOrder(), entity.getActive());
    }
}
