package com.hanoisurvey.api.interfaces.rest.pages.admin;

import com.hanoisurvey.api.application.pages.PageService;
import com.hanoisurvey.api.application.pages.PageCommand;
import com.hanoisurvey.api.interfaces.rest.pages.dto.PageResponse;
import com.hanoisurvey.api.interfaces.rest.pages.dto.PageUpsertRequest;
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
@RequestMapping("/api/admin/v1/pages")
public class PageAdminController {

    private final PageService pageService;

    public PageAdminController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping
    public ApiResponse<List<PageResponse>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var pageData = pageService.getAdminPage(PageRequest.of(page - 1, size));
        var data = pageData.getContent().stream().map(PageResponse::from).toList();
        return ApiResponse.ok(data, PagingMeta.from(pageData));
    }

    @GetMapping("/{id}")
    public ApiResponse<PageResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(PageResponse.from(pageService.getAdminById(id)));
    }

    @PostMapping
    public ApiResponse<PageResponse> create(@Valid @RequestBody PageUpsertRequest request) {
        return ApiResponse.ok(PageResponse.from(pageService.create(new PageCommand(
                request.title(),
                request.slug(),
                request.summary(),
                request.content(),
                request.templateCode(),
                request.contentJson(),
                request.status()
        ))));
    }

    @PutMapping("/{id}")
    public ApiResponse<PageResponse> update(@PathVariable Long id, @Valid @RequestBody PageUpsertRequest request) {
        return ApiResponse.ok(PageResponse.from(pageService.update(id, new PageCommand(
                request.title(),
                request.slug(),
                request.summary(),
                request.content(),
                request.templateCode(),
                request.contentJson(),
                request.status()
        ))));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        pageService.delete(id);
        return ApiResponse.ok("Deleted");
    }
}
