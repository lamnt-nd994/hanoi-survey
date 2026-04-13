package com.hanoisurvey.api.application.services;

import com.hanoisurvey.api.domain.services.ServiceDocument;

import java.util.List;

public interface ServiceDocumentRepositoryPort {
    List<ServiceDocument> findByServiceId(Long serviceId);

    void deleteByServiceId(Long serviceId);

    List<ServiceDocument> saveAll(Long serviceId, List<ServiceDocument> documents);
}
