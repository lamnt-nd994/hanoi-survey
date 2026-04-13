package com.hanoisurvey.api.interfaces.rest.projects.admin;

import com.hanoisurvey.api.application.projects.ProjectService;
import com.hanoisurvey.api.application.projects.ProjectCategoryCommand;
import com.hanoisurvey.api.application.projects.ProjectCommand;
import com.hanoisurvey.api.interfaces.rest.projects.dto.ProjectCategoryResponse;
import com.hanoisurvey.api.interfaces.rest.projects.dto.ProjectCategoryUpsertRequest;
import com.hanoisurvey.api.interfaces.rest.projects.dto.ProjectResponse;
import com.hanoisurvey.api.interfaces.rest.projects.dto.ProjectUpsertRequest;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import com.hanoisurvey.api.interfaces.rest.shared.PagingMeta;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/projects")
public class ProjectAdminController {

    private final ProjectService projectService;

    public ProjectAdminController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/categories")
    public ApiResponse<List<ProjectCategoryResponse>> categories() {
        return ApiResponse.ok(projectService.getAllCategories().stream().map(ProjectCategoryResponse::from).toList());
    }

    @PostMapping("/categories")
    public ApiResponse<ProjectCategoryResponse> createCategory(@Valid @RequestBody ProjectCategoryUpsertRequest request) {
        return ApiResponse.ok(ProjectCategoryResponse.from(projectService.createCategory(new ProjectCategoryCommand(
                request.name(),
                request.slug(),
                request.parentId(),
                request.sortOrder(),
                request.active()
        ))));
    }

    @PutMapping("/categories/{id}")
    public ApiResponse<ProjectCategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody ProjectCategoryUpsertRequest request) {
        return ApiResponse.ok(ProjectCategoryResponse.from(projectService.updateCategory(id, new ProjectCategoryCommand(
                request.name(),
                request.slug(),
                request.parentId(),
                request.sortOrder(),
                request.active()
        ))));
    }

    @DeleteMapping("/categories/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Long id) {
        projectService.deleteCategory(id);
        return ApiResponse.ok("Deleted");
    }

    @GetMapping
    public ApiResponse<List<ProjectResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = projectService.getAdminList(PageRequest.of(page - 1, size));
        return ApiResponse.ok(result.getContent().stream().map(ProjectResponse::from).toList(), PagingMeta.from(result));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjectResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(ProjectResponse.from(projectService.getAdminById(id)));
    }

    @PostMapping
    public ApiResponse<ProjectResponse> create(@Valid @RequestBody ProjectUpsertRequest request) {
        return ApiResponse.ok(ProjectResponse.from(projectService.createProject(new ProjectCommand(
                request.categoryId(),
                request.title(),
                request.slug(),
                request.clientName(),
                request.location(),
                request.startedAt(),
                request.completedAt(),
                request.scaleText(),
                request.coverImagePath(),
                request.galleryJson(),
                request.content(),
                request.status()
        ))));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProjectResponse> update(@PathVariable Long id, @Valid @RequestBody ProjectUpsertRequest request) {
        return ApiResponse.ok(ProjectResponse.from(projectService.updateProject(id, new ProjectCommand(
                request.categoryId(),
                request.title(),
                request.slug(),
                request.clientName(),
                request.location(),
                request.startedAt(),
                request.completedAt(),
                request.scaleText(),
                request.coverImagePath(),
                request.galleryJson(),
                request.content(),
                request.status()
        ))));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ApiResponse.ok("Deleted");
    }
}
