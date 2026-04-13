package com.hanoisurvey.api.infrastructure.persistence.services;

import com.hanoisurvey.api.application.services.ServiceDocumentRepositoryPort;
import com.hanoisurvey.api.domain.services.ServiceDocument;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceDocumentRepositoryAdapter implements ServiceDocumentRepositoryPort {

    private final ServiceDocumentRepository repository;
    private final SurveyServiceRepository serviceRepository;

    public ServiceDocumentRepositoryAdapter(ServiceDocumentRepository repository, SurveyServiceRepository serviceRepository) {
        this.repository = repository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<ServiceDocument> findByServiceId(Long serviceId) {
        return repository.findByService_IdOrderBySortOrderAscIdAsc(serviceId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteByServiceId(Long serviceId) {
        repository.deleteByService_Id(serviceId);
    }

    @Override
    public List<ServiceDocument> saveAll(Long serviceId, List<ServiceDocument> documents) {
        if (documents == null || documents.isEmpty()) {
            return List.of();
        }

        SurveyServiceEntity service = serviceRepository.getReferenceById(serviceId);
        List<ServiceDocumentEntity> entities = documents.stream().map(document -> {
            ServiceDocumentEntity entity = new ServiceDocumentEntity();
            entity.setService(service);
            entity.setTitle(document.title());
            entity.setFilePath(document.filePath());
            entity.setSortOrder(document.sortOrder() == null ? 0 : document.sortOrder());
            return entity;
        }).toList();

        return repository.saveAll(entities).stream().map(this::toDomain).toList();
    }

    private ServiceDocument toDomain(ServiceDocumentEntity entity) {
        return new ServiceDocument(
                entity.getId(),
                entity.getService().getId(),
                entity.getTitle(),
                entity.getFilePath(),
                entity.getSortOrder(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
