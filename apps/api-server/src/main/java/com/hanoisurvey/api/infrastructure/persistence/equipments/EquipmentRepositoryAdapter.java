package com.hanoisurvey.api.infrastructure.persistence.equipments;

import com.hanoisurvey.api.application.equipments.EquipmentRepositoryPort;
import com.hanoisurvey.api.domain.equipments.Equipment;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EquipmentRepositoryAdapter implements EquipmentRepositoryPort {

    private final EquipmentCategoryRepository categoryRepository;
    private final EquipmentRepository repository;

    public EquipmentRepositoryAdapter(EquipmentCategoryRepository categoryRepository, EquipmentRepository repository) {
        this.categoryRepository = categoryRepository;
        this.repository = repository;
    }

    @Override
    public org.springframework.data.domain.Page<Equipment> findPublished(Pageable pageable) {
        return repository.findByStatus(ContentStatus.PUBLISHED, pageable).map(this::toDomain);
    }

    @Override
    public org.springframework.data.domain.Page<Equipment> findPublishedByCategorySlug(String categorySlug, Pageable pageable) {
        return repository.findByStatusAndCategory_Slug(ContentStatus.PUBLISHED, categorySlug, pageable).map(this::toDomain);
    }

    @Override
    public Optional<Equipment> findPublishedBySlug(String slug) {
        return repository.findBySlugAndStatus(slug, ContentStatus.PUBLISHED).map(this::toDomain);
    }

    @Override
    public org.springframework.data.domain.Page<Equipment> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Optional<Equipment> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Equipment save(Equipment equipment) {
        EquipmentEntity entity = equipment.id() == null ? new EquipmentEntity() : repository.findById(equipment.id()).orElseGet(EquipmentEntity::new);
        entity.setId(equipment.id());
        entity.setCategory(categoryRepository.findById(equipment.categoryId()).orElseThrow());
        entity.setName(equipment.name());
        entity.setSlug(equipment.slug());
        entity.setModel(equipment.model());
        entity.setManufacturer(equipment.manufacturer());
        entity.setOrigin(equipment.origin());
        entity.setUnit(equipment.unit());
        entity.setQuantity(equipment.quantity());
        entity.setProductionYear(equipment.productionYear());
        entity.setDescription(equipment.description());
        entity.setCoverImagePath(equipment.coverImagePath());
        entity.setStatus(equipment.status());
        entity.setSortOrder(equipment.sortOrder());
        return toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Equipment toDomain(EquipmentEntity entity) {
        return new Equipment(entity.getId(), entity.getCategory().getId(), entity.getCategory().getName(), entity.getName(), entity.getSlug(), entity.getModel(), entity.getManufacturer(), entity.getOrigin(), entity.getUnit(), entity.getQuantity(), entity.getProductionYear(), entity.getDescription(), entity.getCoverImagePath(), entity.getStatus(), entity.getSortOrder(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
