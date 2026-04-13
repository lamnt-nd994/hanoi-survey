package com.hanoisurvey.api.application.projects;

import com.hanoisurvey.api.domain.projects.Project;
import com.hanoisurvey.api.domain.projects.ProjectCategory;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import com.hanoisurvey.api.domain.shared.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectCategoryRepositoryPort categoryRepository;
    private final ProjectRepositoryPort projectRepository;

    public ProjectService(ProjectCategoryRepositoryPort categoryRepository, ProjectRepositoryPort projectRepository) {
        this.categoryRepository = categoryRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<ProjectCategory> getActiveCategories() {
        return categoryRepository.findAll().stream().filter(ProjectCategory::active).toList();
    }

    @Transactional(readOnly = true)
    public List<ProjectCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public ProjectCategory createCategory(ProjectCategoryCommand command) {
        return categoryRepository.save(new ProjectCategory(null, command.name(), command.slug(), command.parentId(), command.sortOrder() == null ? 0 : command.sortOrder(), command.active() == null || command.active()));
    }

    @Transactional
    public ProjectCategory updateCategory(Long id, ProjectCategoryCommand command) {
        categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục dự án"));
        return categoryRepository.save(new ProjectCategory(id, command.name(), command.slug(), command.parentId(), command.sortOrder() == null ? 0 : command.sortOrder(), command.active() == null || command.active()));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy danh mục dự án");
        }
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Project getPublicBySlug(String slug) {
        return projectRepository.findPublishedBySlug(slug).orElseThrow(() -> new NotFoundException("Không tìm thấy dự án"));
    }

    @Transactional(readOnly = true)
    public Page<Project> getPublicList(String categorySlug, Pageable pageable) {
        return projectRepository.findPublished(categorySlug, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Project> getAdminList(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Project getAdminById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy dự án"));
    }

    @Transactional
    public Project createProject(ProjectCommand command) {
        categoryRepository.findById(command.categoryId()).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục dự án"));
        return projectRepository.save(toDomain(command, null));
    }

    @Transactional
    public Project updateProject(Long id, ProjectCommand command) {
        projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy dự án"));
        categoryRepository.findById(command.categoryId()).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục dự án"));
        return projectRepository.save(toDomain(command, id));
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy dự án");
        }
        projectRepository.deleteById(id);
    }

    private Project toDomain(ProjectCommand command, Long id) {
        return new Project(
                id,
                command.categoryId(),
                null,
                command.title(),
                command.slug(),
                command.clientName(),
                command.location(),
                command.startedAt(),
                command.completedAt(),
                command.scaleText(),
                command.coverImagePath(),
                command.galleryJson(),
                command.content(),
                command.status(),
                command.status() == ContentStatus.PUBLISHED ? LocalDateTime.now() : null,
                null,
                null
        );
    }
}
