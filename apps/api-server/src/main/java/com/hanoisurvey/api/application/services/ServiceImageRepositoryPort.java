package com.hanoisurvey.api.application.services;

import com.hanoisurvey.api.domain.services.ServiceImage;

import java.util.List;

public interface ServiceImageRepositoryPort {
    List<ServiceImage> findByServiceId(Long serviceId);

    void deleteByServiceId(Long serviceId);

    List<ServiceImage> saveAll(Long serviceId, List<ServiceImage> images);
}
