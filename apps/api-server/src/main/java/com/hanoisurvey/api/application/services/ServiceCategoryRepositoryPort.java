package com.hanoisurvey.api.application.services;

import com.hanoisurvey.api.domain.services.ServiceCategory;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryRepositoryPort {
    List<ServiceCategory> findAll();

    Optional<ServiceCategory> findById(Long id);

    boolean existsById(Long id);

    ServiceCategory save(ServiceCategory category);

    void deleteById(Long id);
}
