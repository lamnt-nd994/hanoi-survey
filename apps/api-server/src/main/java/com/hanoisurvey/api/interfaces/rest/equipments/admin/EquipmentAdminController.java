package com.hanoisurvey.api.interfaces.rest.equipments.admin;

import com.hanoisurvey.api.application.equipments.EquipmentService;
import com.hanoisurvey.api.application.equipments.EquipmentCategoryCommand;
import com.hanoisurvey.api.application.equipments.EquipmentCommand;
import com.hanoisurvey.api.interfaces.rest.equipments.dto.EquipmentCategoryResponse;
import com.hanoisurvey.api.interfaces.rest.equipments.dto.EquipmentCategoryUpsertRequest;
import com.hanoisurvey.api.interfaces.rest.equipments.dto.EquipmentResponse;
import com.hanoisurvey.api.interfaces.rest.equipments.dto.EquipmentUpsertRequest;
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
@RequestMapping("/api/admin/v1/equipments")
public class EquipmentAdminController {

    private final EquipmentService equipmentService;

    public EquipmentAdminController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public ApiResponse<List<EquipmentResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = equipmentService.getAdminList(PageRequest.of(page - 1, size));
        return ApiResponse.ok(result.getContent().stream().map(EquipmentResponse::from).toList(), PagingMeta.from(result));
    }

    @GetMapping("/{id:[0-9]+}")
    public ApiResponse<EquipmentResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(EquipmentResponse.from(equipmentService.getAdminById(id)));
    }

    @PostMapping
    public ApiResponse<EquipmentResponse> create(@Valid @RequestBody EquipmentUpsertRequest request) {
        return ApiResponse.ok(EquipmentResponse.from(equipmentService.create(new EquipmentCommand(
                request.categoryId(),
                request.name(),
                request.slug(),
                request.model(),
                request.manufacturer(),
                request.origin(),
                request.unit(),
                request.quantity(),
                request.productionYear(),
                request.description(),
                request.coverImagePath(),
                request.status(),
                request.sortOrder()
        ))));
    }

    @PutMapping("/{id:[0-9]+}")
    public ApiResponse<EquipmentResponse> update(@PathVariable Long id, @Valid @RequestBody EquipmentUpsertRequest request) {
        return ApiResponse.ok(EquipmentResponse.from(equipmentService.update(id, new EquipmentCommand(
                request.categoryId(),
                request.name(),
                request.slug(),
                request.model(),
                request.manufacturer(),
                request.origin(),
                request.unit(),
                request.quantity(),
                request.productionYear(),
                request.description(),
                request.coverImagePath(),
                request.status(),
                request.sortOrder()
        ))));
    }

    @DeleteMapping("/{id:[0-9]+}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return ApiResponse.ok("Deleted");
    }

    @GetMapping("/categories")
    public ApiResponse<List<EquipmentCategoryResponse>> categories() {
        return ApiResponse.ok(equipmentService.getAllCategories().stream().map(EquipmentCategoryResponse::from).toList());
    }

    @PostMapping("/categories")
    public ApiResponse<EquipmentCategoryResponse> createCategory(@Valid @RequestBody EquipmentCategoryUpsertRequest request) {
        return ApiResponse.ok(EquipmentCategoryResponse.from(equipmentService.createCategory(new EquipmentCategoryCommand(
                request.name(),
                request.slug(),
                request.parentId(),
                request.icon(),
                request.sortOrder(),
                request.active()
        ))));
    }

    @PutMapping("/categories/{id}")
    public ApiResponse<EquipmentCategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody EquipmentCategoryUpsertRequest request) {
        return ApiResponse.ok(EquipmentCategoryResponse.from(equipmentService.updateCategory(id, new EquipmentCategoryCommand(
                request.name(),
                request.slug(),
                request.parentId(),
                request.icon(),
                request.sortOrder(),
                request.active()
        ))));
    }

    @DeleteMapping("/categories/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Long id) {
        equipmentService.deleteCategory(id);
        return ApiResponse.ok("Deleted");
    }
}
