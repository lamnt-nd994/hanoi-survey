package com.hanoisurvey.api.interfaces.rest.menus.admin;

import com.hanoisurvey.api.application.menus.MenuService;
import com.hanoisurvey.api.application.menus.MenuCommand;
import com.hanoisurvey.api.application.menus.MenuItemCommand;
import com.hanoisurvey.api.interfaces.rest.menus.dto.MenuItemRequest;
import com.hanoisurvey.api.interfaces.rest.menus.dto.MenuItemResponse;
import com.hanoisurvey.api.interfaces.rest.menus.dto.MenuRequest;
import com.hanoisurvey.api.interfaces.rest.menus.dto.MenuResponse;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/menus")
public class MenuAdminController {

    private final MenuService menuService;

    public MenuAdminController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public ApiResponse<List<MenuResponse>> getMenus() {
        return ApiResponse.ok(menuService.getAllMenus().stream().map(MenuResponse::from).toList());
    }

    @PostMapping
    public ApiResponse<MenuResponse> createMenu(@Valid @RequestBody MenuRequest request) {
        return ApiResponse.ok(MenuResponse.from(menuService.createMenu(new MenuCommand(
                request.code(),
                request.name(),
                request.position()
        ))));
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ApiResponse<MenuResponse> updateMenu(@PathVariable Long id, @Valid @RequestBody MenuRequest request) {
        return ApiResponse.ok(MenuResponse.from(menuService.updateMenu(id, new MenuCommand(
                request.code(),
                request.name(),
                request.position()
        ))));
    }

    @GetMapping("/{code}/items")
    public ApiResponse<List<MenuItemResponse>> getItems(@PathVariable String code) {
        return ApiResponse.ok(menuService.getItemsByMenuCode(code).stream().map(MenuItemResponse::from).toList());
    }

    @PostMapping("/items")
    public ApiResponse<MenuItemResponse> createItem(@Valid @RequestBody MenuItemRequest request) {
        return ApiResponse.ok(MenuItemResponse.from(menuService.createItem(new MenuItemCommand(
                request.menuId(),
                request.parentId(),
                request.title(),
                request.itemType(),
                request.refId(),
                request.url(),
                request.icon(),
                request.sortOrder(),
                request.visible()
        ))));
    }

    @org.springframework.web.bind.annotation.PutMapping("/items/{id}")
    public ApiResponse<MenuItemResponse> updateItem(@PathVariable Long id, @Valid @RequestBody MenuItemRequest request) {
        return ApiResponse.ok(MenuItemResponse.from(menuService.updateItem(id, new MenuItemCommand(
                request.menuId(),
                request.parentId(),
                request.title(),
                request.itemType(),
                request.refId(),
                request.url(),
                request.icon(),
                request.sortOrder(),
                request.visible()
        ))));
    }

    @DeleteMapping("/items/{id}")
    public ApiResponse<String> deleteItem(@PathVariable Long id) {
        menuService.deleteItem(id);
        return ApiResponse.ok("Deleted");
    }
}
