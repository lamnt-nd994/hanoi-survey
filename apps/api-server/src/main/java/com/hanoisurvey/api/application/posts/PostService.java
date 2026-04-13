package com.hanoisurvey.api.application.posts;

import com.hanoisurvey.api.domain.posts.Post;
import com.hanoisurvey.api.domain.posts.PostCategory;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import com.hanoisurvey.api.domain.shared.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostCategoryRepositoryPort categoryRepository;
    private final PostRepositoryPort postRepository;

    public PostService(PostCategoryRepositoryPort categoryRepository, PostRepositoryPort postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<PostCategory> getActiveCategories() {
        return categoryRepository.findAll().stream().filter(PostCategory::active).toList();
    }

    @Transactional(readOnly = true)
    public List<PostCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public PostCategory createCategory(PostCategoryCommand command) {
        return categoryRepository.save(new PostCategory(null, command.name(), command.slug(), command.parentId(), command.sortOrder() == null ? 0 : command.sortOrder(), command.active() == null || command.active()));
    }

    @Transactional
    public PostCategory updateCategory(Long id, PostCategoryCommand command) {
        categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục bài viết"));
        return categoryRepository.save(new PostCategory(id, command.name(), command.slug(), command.parentId(), command.sortOrder() == null ? 0 : command.sortOrder(), command.active() == null || command.active()));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy danh mục bài viết");
        }
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Post getPublicBySlug(String slug) {
        return postRepository.findPublishedBySlug(slug).orElseThrow(() -> new NotFoundException("Không tìm thấy bài viết"));
    }

    @Transactional(readOnly = true)
    public Page<Post> getPublicList(String categorySlug, Pageable pageable) {
        return postRepository.findPublished(categorySlug, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Post> getAdminList(String keyword, Long categoryId, Pageable pageable) {
        return postRepository.findAllFiltered(keyword, categoryId, pageable);
    }

    @Transactional(readOnly = true)
    public Post getAdminById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy bài viết"));
    }

    @Transactional
    public Post createPost(PostCommand command) {
        categoryRepository.findById(command.categoryId()).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục bài viết"));
        return postRepository.save(toDomain(command, null));
    }

    @Transactional
    public Post updatePost(Long id, PostCommand command) {
        postRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy bài viết"));
        categoryRepository.findById(command.categoryId()).orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục bài viết"));
        return postRepository.save(toDomain(command, id));
    }

    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy bài viết");
        }
        postRepository.deleteById(id);
    }

    private Post toDomain(PostCommand command, Long id) {
        return new Post(
                id,
                command.categoryId(),
                null,
                command.title(),
                command.slug(),
                command.excerpt(),
                command.content(),
                command.coverImagePath(),
                command.status(),
                command.status() == ContentStatus.PUBLISHED ? LocalDateTime.now() : null,
                null,
                null
        );
    }
}
