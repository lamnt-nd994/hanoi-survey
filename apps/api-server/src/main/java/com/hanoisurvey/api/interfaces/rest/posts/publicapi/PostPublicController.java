package com.hanoisurvey.api.interfaces.rest.posts.publicapi;

import com.hanoisurvey.api.application.posts.PostService;
import com.hanoisurvey.api.interfaces.rest.posts.dto.PostCategoryResponse;
import com.hanoisurvey.api.interfaces.rest.posts.dto.PostResponse;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import com.hanoisurvey.api.interfaces.rest.shared.PagingMeta;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/v1/posts")
public class PostPublicController {

    private final PostService postService;

    public PostPublicController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/categories")
    public ApiResponse<List<PostCategoryResponse>> categories() {
        return ApiResponse.ok(postService.getActiveCategories().stream().map(PostCategoryResponse::from).toList());
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> list(
            @RequestParam(required = false) String categorySlug,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = postService.getPublicList(categorySlug, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "publishedAt", "createdAt")));
        return ApiResponse.ok(result.getContent().stream().map(PostResponse::from).toList(), PagingMeta.from(result));
    }

    @GetMapping("/{slug}")
    public ApiResponse<PostResponse> detail(@PathVariable String slug) {
        return ApiResponse.ok(PostResponse.from(postService.getPublicBySlug(slug)));
    }
}
