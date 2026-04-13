package com.hanoisurvey.api.infrastructure.persistence.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceDocumentRepository extends JpaRepository<ServiceDocumentEntity, Long> {
    List<ServiceDocumentEntity> findByService_IdOrderBySortOrderAscIdAsc(Long serviceId);

    void deleteByService_Id(Long serviceId);
}
