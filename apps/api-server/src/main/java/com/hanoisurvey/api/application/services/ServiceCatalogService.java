package com.hanoisurvey.api.application.services;

import com.hanoisurvey.api.domain.services.ServiceCategory;
import com.hanoisurvey.api.domain.services.ServiceDocument;
import com.hanoisurvey.api.domain.services.ServiceImage;
import com.hanoisurvey.api.domain.services.SurveyService;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import com.hanoisurvey.api.domain.shared.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class ServiceCatalogService {

    private final ServiceCategoryRepositoryPort categoryRepository;
    private final SurveyServiceRepositoryPort serviceRepository;
    private final ServiceDocumentRepositoryPort documentRepository;
    private final ServiceImageRepositoryPort imageRepository;

    public ServiceCatalogService(ServiceCategoryRepositoryPort categoryRepository, SurveyServiceRepositoryPort serviceRepository, ServiceDocumentRepositoryPort documentRepository, ServiceImageRepositoryPort imageRepository) {
        this.categoryRepository = categoryRepository;
        this.serviceRepository = serviceRepository;
        this.documentRepository = documentRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional(readOnly = true)
    public List<ServiceCategory> getActiveCategories() {
        return categoryRepository.findAll().stream().filter(ServiceCategory::active).toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public ServiceCategory createCategory(ServiceCategoryCommand command) {
        return categoryRepository.save(new ServiceCategory(null, command.name(), command.slug(), command.parentId(), command.sortOrder() == null ? 0 : command.sortOrder(), command.active() == null || command.active()));
    }

    @Transactional
    public ServiceCategory updateCategory(Long id, ServiceCategoryCommand command) {
        categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục lĩnh vực"));
        return categoryRepository.save(new ServiceCategory(id, command.name(), command.slug(), command.parentId(), command.sortOrder() == null ? 0 : command.sortOrder(), command.active() == null || command.active()));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy danh mục lĩnh vực");
        }
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SurveyService getPublicBySlug(String slug) {
        return serviceRepository.findPublishedBySlug(slug).orElseThrow(() -> new NotFoundException("Không tìm thấy lĩnh vực"));
    }

    @Transactional(readOnly = true)
    public Page<SurveyService> getPublicList(String categorySlug, Pageable pageable) {
        return serviceRepository.findPublished(null, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SurveyService> getAdminList(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public SurveyService getAdminById(Long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy lĩnh vực"));
    }

    @Transactional(readOnly = true)
    public List<ServiceDocument> getDocumentsByServiceId(Long serviceId) {
        return documentRepository.findByServiceId(serviceId);
    }

    @Transactional(readOnly = true)
    public List<ServiceImage> getImagesByService(SurveyService service) {
        List<ServiceImage> images = imageRepository.findByServiceId(service.id());
        if (!images.isEmpty()) {
            return images;
        }

        return fallbackImagesFromGalleryJson(service);
    }

    @Transactional
    public SurveyService createService(SurveyServiceCommand command) {
        validateOptionalCategory(command.categoryId());
        SurveyService created = serviceRepository.save(toDomain(command, null));
        replaceDocuments(created.id(), command.documents());
        replaceImages(created.id(), command.images());
        return created;
    }

    @Transactional
    public SurveyService updateService(Long id, SurveyServiceCommand command) {
        serviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy lĩnh vực"));
        validateOptionalCategory(command.categoryId());
        SurveyService updated = serviceRepository.save(toDomain(command, id));
        replaceDocuments(id, command.documents());
        replaceImages(id, command.images());
        return updated;
    }

    @Transactional
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy lĩnh vực");
        }
        documentRepository.deleteByServiceId(id);
        imageRepository.deleteByServiceId(id);
        serviceRepository.deleteById(id);
    }

    private void replaceDocuments(Long serviceId, List<ServiceDocumentCommand> documents) {
        documentRepository.deleteByServiceId(serviceId);
        List<ServiceDocumentCommand> normalized = normalizeDocuments(documents);
        if (normalized.isEmpty()) {
            return;
        }

        documentRepository.saveAll(serviceId, normalized.stream().map(item -> new ServiceDocument(
                null,
                serviceId,
                item.title(),
                item.filePath(),
                item.sortOrder(),
                null,
                null
        )).toList());
    }

    private List<ServiceDocumentCommand> normalizeDocuments(List<ServiceDocumentCommand> documents) {
        if (documents == null || documents.isEmpty()) {
            return List.of();
        }

        return documents.stream()
                .filter(item -> item != null && item.title() != null && !item.title().isBlank() && item.filePath() != null && !item.filePath().isBlank())
                .sorted(Comparator.comparing(item -> item.sortOrder() == null ? Integer.MAX_VALUE : item.sortOrder()))
                .map(item -> new ServiceDocumentCommand(item.title().trim(), item.filePath().trim(), item.sortOrder() == null ? 0 : item.sortOrder()))
                .toList();
    }

    private void replaceImages(Long serviceId, List<ServiceImageCommand> images) {
        imageRepository.deleteByServiceId(serviceId);
        List<ServiceImageCommand> normalized = normalizeImages(images);
        if (normalized.isEmpty()) {
            return;
        }

        imageRepository.saveAll(serviceId, normalized.stream().map(item -> new ServiceImage(
                null,
                serviceId,
                item.imagePath(),
                item.altText(),
                item.caption(),
                item.sortOrder(),
                null,
                null
        )).toList());
    }

    private List<ServiceImageCommand> normalizeImages(List<ServiceImageCommand> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        return images.stream()
                .filter(item -> item != null && item.imagePath() != null && !item.imagePath().isBlank())
                .sorted(Comparator.comparing(item -> item.sortOrder() == null ? Integer.MAX_VALUE : item.sortOrder()))
                .map(item -> new ServiceImageCommand(
                        item.imagePath().trim(),
                        item.altText() == null ? null : item.altText().trim(),
                        item.caption() == null ? null : item.caption().trim(),
                        item.sortOrder() == null ? 0 : item.sortOrder()
                ))
                .toList();
    }

    private List<ServiceImage> fallbackImagesFromGalleryJson(SurveyService service) {
        String galleryJson = service.galleryJson();
        if (galleryJson == null || galleryJson.isBlank()) {
            return List.of();
        }

        List<String> paths = Arrays.stream(galleryJson.split("\\R"))
                .map(String::trim)
                .filter(path -> !path.isBlank())
                .distinct()
                .toList();

        List<ServiceImage> images = new ArrayList<>();
        for (int index = 0; index < paths.size(); index += 1) {
            images.add(new ServiceImage(
                    null,
                    service.id(),
                    paths.get(index),
                    service.title(),
                    null,
                    index,
                    null,
                    null
            ));
        }
        return images;
    }

    private void validateOptionalCategory(Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            return;
        }

        categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục lĩnh vực"));
    }

    private SurveyService toDomain(SurveyServiceCommand command, Long id) {
        return new SurveyService(
                id,
                command.categoryId(),
                null,
                command.title(),
                command.slug(),
                command.overview(),
                command.content(),
                command.icon(),
                command.coverImagePath(),
                command.galleryJson(),
                command.status(),
                command.status() == ContentStatus.PUBLISHED ? LocalDateTime.now() : null,
                null,
                null
        );
    }
}
