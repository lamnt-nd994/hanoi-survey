package com.hanoisurvey.api.interfaces.rest.pages.publicapi;

import com.hanoisurvey.api.application.pages.PageService;
import com.hanoisurvey.api.interfaces.rest.pages.dto.PageResponse;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/v1/pages")
public class PagePublicController {

    private final PageService pageService;

    public PagePublicController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/{slug}")
    public ApiResponse<PageResponse> getBySlug(@PathVariable String slug) {
        return ApiResponse.ok(PageResponse.from(pageService.getPublicBySlug(slug)));
    }
}
