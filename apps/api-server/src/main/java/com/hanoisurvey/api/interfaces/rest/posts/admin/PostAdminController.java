package com.hanoisurvey.api.interfaces.rest.posts.admin;

import com.hanoisurvey.api.application.posts.PostService;
import com.hanoisurvey.api.application.posts.PostCategoryCommand;
import com.hanoisurvey.api.application.posts.PostCommand;
import com.hanoisurvey.api.interfaces.rest.posts.dto.PostCategoryResponse;
import com.hanoisurvey.api.interfaces.rest.posts.dto.PostCategoryUpsertRequest;
import com.hanoisurvey.api.interfaces.rest.posts.dto.PostResponse;
import com.hanoisurvey.api.interfaces.rest.posts.dto.PostUpsertRequest;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import com.hanoisurvey.api.interfaces.rest.shared.PagingMeta;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/api/admin/v1/posts")
public class PostAdminController {

    private final PostService postService;

    public PostAdminController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/categories")
    public ApiResponse<List<PostCategoryResponse>> categories() {
        return ApiResponse.ok(postService.getAllCategories().stream().map(PostCategoryResponse::from).toList());
    }

    @PostMapping("/categories")
    public ApiResponse<PostCategoryResponse> createCategory(@Valid @RequestBody PostCategoryUpsertRequest request) {
        return ApiResponse.ok(PostCategoryResponse.from(postService.createCategory(new PostCategoryCommand(
                request.name(),
                request.slug(),
                request.parentId(),
                request.sortOrder(),
                request.active()
        ))));
    }

    @PutMapping("/categories/{id}")
    public ApiResponse<PostCategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody PostCategoryUpsertRequest request) {
        return ApiResponse.ok(PostCategoryResponse.from(postService.updateCategory(id, new PostCategoryCommand(
                request.name(),
                request.slug(),
                request.parentId(),
                request.sortOrder(),
                request.active()
        ))));
    }

    @DeleteMapping("/categories/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Long id) {
        postService.deleteCategory(id);
        return ApiResponse.ok("Deleted");
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId
    ) {
        var result = postService.getAdminList(keyword, categoryId, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        return ApiResponse.ok(result.getContent().stream().map(PostResponse::from).toList(), PagingMeta.from(result));
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok(PostResponse.from(postService.getAdminById(id)));
    }

    @PostMapping
    public ApiResponse<PostResponse> create(@Valid @RequestBody PostUpsertRequest request) {
        return ApiResponse.ok(PostResponse.from(postService.createPost(new PostCommand(
                request.categoryId(),
                request.title(),
                request.slug(),
                request.excerpt(),
                request.content(),
                request.coverImagePath(),
                request.status()
        ))));
    }

    @PutMapping("/{id}")
    public ApiResponse<PostResponse> update(@PathVariable Long id, @Valid @RequestBody PostUpsertRequest request) {
        return ApiResponse.ok(PostResponse.from(postService.updatePost(id, new PostCommand(
                request.categoryId(),
                request.title(),
                request.slug(),
                request.excerpt(),
                request.content(),
                request.coverImagePath(),
                request.status()
        ))));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        postService.deletePost(id);
        return ApiResponse.ok("Deleted");
    }
}
