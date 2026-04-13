package com.hanoisurvey.api.infrastructure.persistence.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceImageRepository extends JpaRepository<ServiceImageEntity, Long> {
    List<ServiceImageEntity> findByService_IdOrderBySortOrderAscIdAsc(Long serviceId);

    void deleteByService_Id(Long serviceId);
}
