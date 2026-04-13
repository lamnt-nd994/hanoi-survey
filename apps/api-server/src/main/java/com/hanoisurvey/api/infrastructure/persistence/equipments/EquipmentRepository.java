package com.hanoisurvey.api.infrastructure.persistence.equipments;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {
    Optional<EquipmentEntity> findBySlugAndStatus(String slug, ContentStatus status);

    @EntityGraph(attributePaths = "category")
    Page<EquipmentEntity> findByStatus(ContentStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<EquipmentEntity> findByStatusAndCategory_Slug(ContentStatus status, String categorySlug, Pageable pageable);
}
