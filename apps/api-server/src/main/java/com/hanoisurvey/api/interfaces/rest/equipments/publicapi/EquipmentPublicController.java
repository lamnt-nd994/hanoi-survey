package com.hanoisurvey.api.interfaces.rest.equipments.publicapi;

import com.hanoisurvey.api.application.equipments.EquipmentService;
import com.hanoisurvey.api.interfaces.rest.equipments.dto.EquipmentCategoryResponse;
import com.hanoisurvey.api.interfaces.rest.equipments.dto.EquipmentResponse;
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
@RequestMapping("/api/public/v1/equipments")
public class EquipmentPublicController {

    private final EquipmentService equipmentService;

    public EquipmentPublicController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public ApiResponse<List<EquipmentResponse>> list(
            @RequestParam(required = false) String categorySlug,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = equipmentService.getPublicList(categorySlug, PageRequest.of(page - 1, size));
        return ApiResponse.ok(result.getContent().stream().map(EquipmentResponse::from).toList(), PagingMeta.from(result));
    }

    @GetMapping("/{slug}")
    public ApiResponse<EquipmentResponse> detail(@PathVariable String slug) {
        return ApiResponse.ok(EquipmentResponse.from(equipmentService.getPublicBySlug(slug)));
    }

    @GetMapping("/categories")
    public ApiResponse<List<EquipmentCategoryResponse>> categories() {
        return ApiResponse.ok(equipmentService.getActiveCategories().stream().map(EquipmentCategoryResponse::from).toList());
    }
}
