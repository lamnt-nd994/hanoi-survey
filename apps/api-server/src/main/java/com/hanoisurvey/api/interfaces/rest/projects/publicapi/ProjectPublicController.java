package com.hanoisurvey.api.interfaces.rest.projects.publicapi;

import com.hanoisurvey.api.application.projects.ProjectService;
import com.hanoisurvey.api.interfaces.rest.projects.dto.ProjectCategoryResponse;
import com.hanoisurvey.api.interfaces.rest.projects.dto.ProjectResponse;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import com.hanoisurvey.api.interfaces.rest.shared.PagingMeta;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/v1/projects")
public class ProjectPublicController {

    private final ProjectService projectService;

    public ProjectPublicController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/categories")
    public ApiResponse<List<ProjectCategoryResponse>> categories() {
        return ApiResponse.ok(projectService.getActiveCategories().stream().map(ProjectCategoryResponse::from).toList());
    }

    @GetMapping
    public ApiResponse<List<ProjectResponse>> list(
            @RequestParam(required = false) String categorySlug,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = projectService.getPublicList(categorySlug, PageRequest.of(page - 1, size));
        return ApiResponse.ok(result.getContent().stream().map(ProjectResponse::from).toList(), PagingMeta.from(result));
    }

    @GetMapping("/{slug}")
    public ApiResponse<ProjectResponse> detail(@PathVariable String slug) {
        return ApiResponse.ok(ProjectResponse.from(projectService.getPublicBySlug(slug)));
    }
}
