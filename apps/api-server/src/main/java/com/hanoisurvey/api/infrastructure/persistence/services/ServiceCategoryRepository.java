package com.hanoisurvey.api.infrastructure.persistence.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategoryEntity, Long> {
    Optional<ServiceCategoryEntity> findBySlug(String slug);
}
