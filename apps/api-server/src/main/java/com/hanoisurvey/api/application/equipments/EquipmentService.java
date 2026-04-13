package com.hanoisurvey.api.application.equipments;

import com.hanoisurvey.api.domain.equipments.Equipment;
import com.hanoisurvey.api.domain.equipments.EquipmentCategory;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import com.hanoisurvey.api.domain.shared.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipmentService {

    private final EquipmentCategoryRepositoryPort categoryRepository;
    private final EquipmentRepositoryPort repository;

    public EquipmentService(EquipmentCategoryRepositoryPort categoryRepository, EquipmentRepositoryPort repository) {
        this.categoryRepository = categoryRepository;
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EquipmentCategory> getActiveCategories() {
        return categoryRepository.findAll().stream().filter(EquipmentCategory::active).toList();
    }

    @Transactional(readOnly = true)
    public List<EquipmentCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public EquipmentCategory createCategory(EquipmentCategoryCommand command) {
        return categoryRepository.save(new EquipmentCategory(null, command.name(), command.slug(), command.parentId(), command.icon(), command.sortOrder() == null ? 0 : command.sortOrder(), command.active() == null || command.active()));
    }

    @Transactional
    public EquipmentCategory updateCategory(Long id, EquipmentCategoryCommand command) {
        categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục thiết bị"));
        return categoryRepository.save(new EquipmentCategory(id, command.name(), command.slug(), command.parentId(), command.icon(), command.sortOrder() == null ? 0 : command.sortOrder(), command.active() == null || command.active()));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy danh mục thiết bị");
        }
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Equipment> getPublicList(String categorySlug, Pageable pageable) {
        if (categorySlug == null || categorySlug.isBlank()) {
            return repository.findPublished(pageable);
        }
        return repository.findPublishedByCategorySlug(categorySlug, pageable);
    }

    @Transactional(readOnly = true)
    public Equipment getPublicBySlug(String slug) {
        return repository.findPublishedBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy máy móc thiết bị"));
    }

    @Transactional(readOnly = true)
    public Page<Equipment> getAdminList(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Equipment getAdminById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy máy móc thiết bị"));
    }

    @Transactional
    public Equipment create(EquipmentCommand command) {
        categoryRepository.findById(command.categoryId()).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục thiết bị"));
        return repository.save(toDomain(command, null, null));
    }

    @Transactional
    public Equipment update(Long id, EquipmentCommand command) {
        var existing = repository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy máy móc thiết bị"));
        categoryRepository.findById(command.categoryId()).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục thiết bị"));
        return repository.save(toDomain(command, id, existing.sortOrder()));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy máy móc thiết bị");
        }
        repository.deleteById(id);
    }

    private Equipment toDomain(EquipmentCommand command, Long id, Integer existingSortOrder) {
        return new Equipment(
                id,
                command.categoryId(),
                null,
                command.name(),
                command.slug(),
                command.model(),
                command.manufacturer(),
                command.origin(),
                command.unit(),
                command.quantity(),
                command.productionYear(),
                command.description(),
                command.coverImagePath(),
                command.status(),
                command.sortOrder() == null ? (existingSortOrder == null ? 0 : existingSortOrder) : command.sortOrder(),
                null,
                null
        );
    }
}
