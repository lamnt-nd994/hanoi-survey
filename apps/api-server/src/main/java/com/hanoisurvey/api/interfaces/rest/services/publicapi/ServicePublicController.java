package com.hanoisurvey.api.interfaces.rest.services.publicapi;

import com.hanoisurvey.api.application.services.ServiceCatalogService;
import com.hanoisurvey.api.interfaces.rest.services.dto.ServiceCategoryResponse;
import com.hanoisurvey.api.interfaces.rest.services.dto.SurveyServiceResponse;
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
@RequestMapping("/api/public/v1/services")
public class ServicePublicController {

    private final ServiceCatalogService serviceCatalogService;

    public ServicePublicController(ServiceCatalogService serviceCatalogService) {
        this.serviceCatalogService = serviceCatalogService;
    }

    @GetMapping("/categories")
    public ApiResponse<List<ServiceCategoryResponse>> categories() {
        return ApiResponse.ok(serviceCatalogService.getActiveCategories().stream().map(ServiceCategoryResponse::from).toList());
    }

    @GetMapping
    public ApiResponse<List<SurveyServiceResponse>> list(
            @RequestParam(required = false) String categorySlug,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = serviceCatalogService.getPublicList(categorySlug, PageRequest.of(page - 1, size));
        return ApiResponse.ok(result.getContent().stream().map(SurveyServiceResponse::from).toList(), PagingMeta.from(result));
    }

    @GetMapping("/{slug}")
    public ApiResponse<SurveyServiceResponse> detail(@PathVariable String slug) {
        var service = serviceCatalogService.getPublicBySlug(slug);
        var documents = serviceCatalogService.getDocumentsByServiceId(service.id()).stream().map(com.hanoisurvey.api.interfaces.rest.services.dto.ServiceDocumentResponse::from).toList();
        var images = serviceCatalogService.getImagesByService(service).stream().map(com.hanoisurvey.api.interfaces.rest.services.dto.ServiceImageResponse::from).toList();
        return ApiResponse.ok(SurveyServiceResponse.from(service, documents, images));
    }
}
