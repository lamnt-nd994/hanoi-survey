package com.hanoisurvey.api.infrastructure.persistence.services;

import com.hanoisurvey.api.application.services.ServiceImageRepositoryPort;
import com.hanoisurvey.api.domain.services.ServiceImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceImageRepositoryAdapter implements ServiceImageRepositoryPort {

    private final ServiceImageRepository repository;
    private final SurveyServiceRepository serviceRepository;

    public ServiceImageRepositoryAdapter(ServiceImageRepository repository, SurveyServiceRepository serviceRepository) {
        this.repository = repository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<ServiceImage> findByServiceId(Long serviceId) {
        return repository.findByService_IdOrderBySortOrderAscIdAsc(serviceId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteByServiceId(Long serviceId) {
        repository.deleteByService_Id(serviceId);
    }

    @Override
    public List<ServiceImage> saveAll(Long serviceId, List<ServiceImage> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        SurveyServiceEntity service = serviceRepository.getReferenceById(serviceId);
        List<ServiceImageEntity> entities = images.stream().map(image -> {
            ServiceImageEntity entity = new ServiceImageEntity();
            entity.setService(service);
            entity.setImagePath(image.imagePath());
            entity.setAltText(image.altText());
            entity.setCaption(image.caption());
            entity.setSortOrder(image.sortOrder() == null ? 0 : image.sortOrder());
            return entity;
        }).toList();

        return repository.saveAll(entities).stream().map(this::toDomain).toList();
    }

    private ServiceImage toDomain(ServiceImageEntity entity) {
        return new ServiceImage(
                entity.getId(),
                entity.getService().getId(),
                entity.getImagePath(),
                entity.getAltText(),
                entity.getCaption(),
                entity.getSortOrder(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
