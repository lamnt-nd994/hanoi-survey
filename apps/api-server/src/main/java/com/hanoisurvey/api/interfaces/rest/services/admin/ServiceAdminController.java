package com.hanoisurvey.api.interfaces.rest.services.admin;

import com.hanoisurvey.api.application.services.ServiceCatalogService;
import com.hanoisurvey.api.application.services.ServiceCategoryCommand;
import com.hanoisurvey.api.application.services.SurveyServiceCommand;
import com.hanoisurvey.api.interfaces.rest.services.dto.ServiceCategoryResponse;
import com.hanoisurvey.api.interfaces.rest.services.dto.ServiceCategoryUpsertRequest;
import com.hanoisurvey.api.interfaces.rest.services.dto.SurveyServiceResponse;
import com.hanoisurvey.api.interfaces.rest.services.dto.SurveyServiceUpsertRequest;
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
@RequestMapping("/api/admin/v1/services")
public class ServiceAdminController {

    private final ServiceCatalogService serviceCatalogService;

    public ServiceAdminController(ServiceCatalogService serviceCatalogService) {
        this.serviceCatalogService = serviceCatalogService;
    }

    @GetMapping("/categories")
    public ApiResponse<List<ServiceCategoryResponse>> categories() {
        return ApiResponse.ok(serviceCatalogService.getAllCategories().stream().map(ServiceCategoryResponse::from).toList());
    }

    @PostMapping("/categories")
    public ApiResponse<ServiceCategoryResponse> createCategory(@Valid @RequestBody ServiceCategoryUpsertRequest request) {
        return ApiResponse.ok(ServiceCategoryResponse.from(serviceCatalogService.createCategory(new ServiceCategoryCommand(
                request.name(),
                request.slug(),
                request.parentId(),
                request.sortOrder(),
                request.active()
        ))));
    }

    @PutMapping("/categories/{id}")
    public ApiResponse<ServiceCategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody ServiceCategoryUpsertRequest request) {
        return ApiResponse.ok(ServiceCategoryResponse.from(serviceCatalogService.updateCategory(id, new ServiceCategoryCommand(
                request.name(),
                request.slug(),
                request.parentId(),
                request.sortOrder(),
                request.active()
        ))));
    }

    @DeleteMapping("/categories/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Long id) {
        serviceCatalogService.deleteCategory(id);
        return ApiResponse.ok("Deleted");
    }

    @GetMapping
    public ApiResponse<List<SurveyServiceResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = serviceCatalogService.getAdminList(PageRequest.of(page - 1, size));
        return ApiResponse.ok(result.getContent().stream().map(SurveyServiceResponse::from).toList(), PagingMeta.from(result));
    }

    @GetMapping("/{id}")
    public ApiResponse<SurveyServiceResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(SurveyServiceResponse.from(serviceCatalogService.getAdminById(id)));
    }

    @PostMapping
    public ApiResponse<SurveyServiceResponse> create(@Valid @RequestBody SurveyServiceUpsertRequest request) {
        return ApiResponse.ok(SurveyServiceResponse.from(serviceCatalogService.createService(new SurveyServiceCommand(
                request.categoryId(),
                request.title(),
                request.slug(),
                request.overview(),
                request.content(),
                request.icon(),
                request.coverImagePath(),
                request.status()
        ))));
    }

    @PutMapping("/{id}")
    public ApiResponse<SurveyServiceResponse> update(@PathVariable Long id, @Valid @RequestBody SurveyServiceUpsertRequest request) {
        return ApiResponse.ok(SurveyServiceResponse.from(serviceCatalogService.updateService(id, new SurveyServiceCommand(
                request.categoryId(),
                request.title(),
                request.slug(),
                request.overview(),
                request.content(),
                request.icon(),
                request.coverImagePath(),
                request.status()
        ))));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        serviceCatalogService.deleteService(id);
        return ApiResponse.ok("Deleted");
    }
}
