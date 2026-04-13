package com.hanoisurvey.api.infrastructure.persistence.equipments;

import com.hanoisurvey.api.application.equipments.EquipmentCategoryRepositoryPort;
import com.hanoisurvey.api.domain.equipments.EquipmentCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EquipmentCategoryRepositoryAdapter implements EquipmentCategoryRepositoryPort {

    private final EquipmentCategoryRepository repository;

    public EquipmentCategoryRepositoryAdapter(EquipmentCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EquipmentCategory> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<EquipmentCategory> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public EquipmentCategory save(EquipmentCategory category) {
        EquipmentCategoryEntity entity = category.id() == null ? new EquipmentCategoryEntity() : repository.findById(category.id()).orElseGet(EquipmentCategoryEntity::new);
        entity.setId(category.id());
        entity.setName(category.name());
        entity.setSlug(category.slug());
        entity.setParent(category.parentId() == null ? null : repository.findById(category.parentId()).orElseThrow());
        entity.setIcon(category.icon());
        entity.setSortOrder(category.sortOrder());
        entity.setActive(category.active());
        return toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private EquipmentCategory toDomain(EquipmentCategoryEntity entity) {
        return new EquipmentCategory(entity.getId(), entity.getName(), entity.getSlug(), entity.getParent() == null ? null : entity.getParent().getId(), entity.getIcon(), entity.getSortOrder(), entity.getActive());
    }
}
