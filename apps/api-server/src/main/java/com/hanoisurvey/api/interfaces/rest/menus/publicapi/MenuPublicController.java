package com.hanoisurvey.api.interfaces.rest.menus.publicapi;

import com.hanoisurvey.api.application.equipments.EquipmentService;
import com.hanoisurvey.api.application.menus.MenuService;
import com.hanoisurvey.api.application.pages.PageService;
import com.hanoisurvey.api.application.posts.PostService;
import com.hanoisurvey.api.application.projects.ProjectService;
import com.hanoisurvey.api.application.services.ServiceCatalogService;
import com.hanoisurvey.api.domain.pages.Page;
import com.hanoisurvey.api.interfaces.rest.menus.dto.MenuItemResponse;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/v1/menus")
public class MenuPublicController {

    private final MenuService menuService;
    private final PageService pageService;
    private final PostService postService;
    private final ServiceCatalogService serviceCatalogService;
    private final ProjectService projectService;
    private final EquipmentService equipmentService;

    public MenuPublicController(
            MenuService menuService,
            PageService pageService,
            PostService postService,
            ServiceCatalogService serviceCatalogService,
            ProjectService projectService,
            EquipmentService equipmentService
    ) {
        this.menuService = menuService;
        this.pageService = pageService;
        this.postService = postService;
        this.serviceCatalogService = serviceCatalogService;
        this.projectService = projectService;
        this.equipmentService = equipmentService;
    }

    @GetMapping("/{code}")
    public ApiResponse<List<MenuItemResponse>> getMenu(@PathVariable String code) {
        List<MenuItemResponse> items = menuService.getItemsByMenuCode(code).stream()
                .filter(item -> Boolean.TRUE.equals(item.visible()))
                .map(item -> MenuItemResponse.from(item, resolveUrl(item.itemType(), item.refId(), item.url())))
                .toList();
        return ApiResponse.ok(items);
    }

    private String resolveUrl(String itemType, Long refId, String url) {
        if (itemType == null || itemType.isBlank()) {
            return normalizeUrl(url);
        }

        return switch (itemType.trim().toUpperCase()) {
            case "PAGE" -> resolvePageUrl(refId, url);
            case "POST" -> refId == null ? normalizeUrl(url) : "/tin-tuc/" + postService.getAdminById(refId).slug();
            case "SERVICE" -> refId == null ? normalizeUrl(url) : "/linh-vuc/" + serviceCatalogService.getAdminById(refId).slug();
            case "PROJECT" -> refId == null ? normalizeUrl(url) : "/du-an/" + projectService.getAdminById(refId).slug();
            case "EQUIPMENT" -> refId == null ? normalizeUrl(url) : "/may-moc/" + equipmentService.getAdminById(refId).slug();
            case "POST_CATEGORY" -> normalizeUrl(url == null || url.isBlank() ? "/tin-tuc" : url);
            case "SERVICE_CATEGORY" -> normalizeUrl(url == null || url.isBlank() ? "/linh-vuc" : url);
            case "PROJECT_CATEGORY" -> normalizeUrl(url == null || url.isBlank() ? "/du-an" : url);
            case "URL", "CUSTOM" -> normalizeUrl(url);
            default -> normalizeUrl(url);
        };
    }

    private String resolvePageUrl(Long refId, String url) {
        if (refId == null) {
            return normalizeUrl(url);
        }

        Page page = pageService.getAdminById(refId);
        return switch (page.slug()) {
            case "gioi-thieu" -> "/gioi-thieu";
            case "lien-he" -> "/lien-he";
            default -> "/trang/" + page.slug();
        };
    }

    private String normalizeUrl(String url) {
        if (url == null || url.isBlank()) {
            return "/";
        }

        String normalized = url.startsWith("http://") || url.startsWith("https://") ? url : "/" + url.replaceFirst("^/+", "");
        return switch (normalized) {
            case "/gioi-thieu-cong-ty" -> "/gioi-thieu";
            case "/dich-vu" -> "/linh-vuc";
            case "/thiet-bi" -> "/may-moc";
            default -> normalized;
        };
    }
}
