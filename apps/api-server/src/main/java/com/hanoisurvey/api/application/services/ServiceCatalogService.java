package com.hanoisurvey.api.application.services;

import com.hanoisurvey.api.domain.services.ServiceCategory;
import com.hanoisurvey.api.domain.services.SurveyService;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import com.hanoisurvey.api.domain.shared.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceCatalogService {

    private static final Logger log = LoggerFactory.getLogger(ServiceCatalogService.class);
    private final ServiceCategoryRepositoryPort categoryRepository;
    private final SurveyServiceRepositoryPort serviceRepository;

    public ServiceCatalogService(ServiceCategoryRepositoryPort categoryRepository, SurveyServiceRepositoryPort serviceRepository) {
        this.categoryRepository = categoryRepository;
        this.serviceRepository = serviceRepository;
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
        log.info(serviceRepository.findPublished(categorySlug, pageable).toString());
        return serviceRepository.findPublished(categorySlug, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SurveyService> getAdminList(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public SurveyService getAdminById(Long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy lĩnh vực"));
    }

    @Transactional
    public SurveyService createService(SurveyServiceCommand command) {
        categoryRepository.findById(command.categoryId()).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục lĩnh vực"));
        return serviceRepository.save(toDomain(command, null));
    }

    @Transactional
    public SurveyService updateService(Long id, SurveyServiceCommand command) {
        serviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy lĩnh vực"));
        categoryRepository.findById(command.categoryId()).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục lĩnh vực"));
        return serviceRepository.save(toDomain(command, id));
    }

    @Transactional
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy lĩnh vực");
        }
        serviceRepository.deleteById(id);
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
                command.status(),
                command.status() == ContentStatus.PUBLISHED ? LocalDateTime.now() : null,
                null,
                null
        );
    }
}
