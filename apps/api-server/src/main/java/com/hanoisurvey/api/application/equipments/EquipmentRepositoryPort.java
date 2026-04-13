package com.hanoisurvey.api.application.equipments;

import com.hanoisurvey.api.domain.equipments.Equipment;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EquipmentRepositoryPort {
    org.springframework.data.domain.Page<Equipment> findPublished(Pageable pageable);

    org.springframework.data.domain.Page<Equipment> findPublishedByCategorySlug(String categorySlug, Pageable pageable);

    Optional<Equipment> findPublishedBySlug(String slug);

    org.springframework.data.domain.Page<Equipment> findAll(Pageable pageable);

    Optional<Equipment> findById(Long id);

    boolean existsById(Long id);

    Equipment save(Equipment equipment);

    void deleteById(Long id);
}
