package com.hanoisurvey.api.application.equipments;

import com.hanoisurvey.api.domain.equipments.EquipmentCategory;

import java.util.List;
import java.util.Optional;

public interface EquipmentCategoryRepositoryPort {
    List<EquipmentCategory> findAll();

    Optional<EquipmentCategory> findById(Long id);

    boolean existsById(Long id);

    EquipmentCategory save(EquipmentCategory category);

    void deleteById(Long id);
}
