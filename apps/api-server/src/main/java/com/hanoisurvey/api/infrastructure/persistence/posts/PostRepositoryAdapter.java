package com.hanoisurvey.api.infrastructure.persistence.posts;

import com.hanoisurvey.api.application.posts.PostRepositoryPort;
import com.hanoisurvey.api.domain.posts.Post;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostRepositoryAdapter implements PostRepositoryPort {

    private final PostRepository repository;
    private final PostCategoryRepository categoryRepository;

    public PostRepositoryAdapter(PostRepository repository, PostCategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Post> findPublishedBySlug(String slug) {
        return repository.findBySlugAndStatus(slug, ContentStatus.PUBLISHED).map(this::toDomain);
    }

    @Override
    public org.springframework.data.domain.Page<Post> findPublished(String categorySlug, Pageable pageable) {
        if (categorySlug == null || categorySlug.isBlank()) {
            return repository.findByStatus(ContentStatus.PUBLISHED, pageable).map(this::toDomain);
        }
        return repository.findByStatusAndCategory_Slug(ContentStatus.PUBLISHED, categorySlug, pageable).map(this::toDomain);
    }

    @Override
    public org.springframework.data.domain.Page<Post> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Post save(Post post) {
        PostEntity entity = post.id() == null ? new PostEntity() : repository.findById(post.id()).orElseGet(PostEntity::new);
        entity.setId(post.id());
        entity.setCategory(categoryRepository.findById(post.categoryId()).orElseThrow());
        entity.setTitle(post.title());
        entity.setSlug(post.slug());
        entity.setExcerpt(post.excerpt());
        entity.setContent(post.content());
        entity.setCoverImagePath(post.coverImagePath());
        entity.setStatus(post.status());
        entity.setPublishedAt(post.publishedAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public org.springframework.data.domain.Page<Post> findAllFiltered(String keyword, Long categoryId, Pageable pageable) {
        String kw = (keyword != null && !keyword.isBlank()) ? keyword : null;
        Long catId = (categoryId != null && categoryId > 0) ? categoryId : null;
        return repository.findByKeywordAndCategoryId(kw, catId, pageable).map(this::toDomain);
    }

    private Post toDomain(PostEntity entity) {
        return new Post(entity.getId(), entity.getCategory().getId(), entity.getCategory().getName(), entity.getTitle(), entity.getSlug(), entity.getExcerpt(), entity.getContent(), entity.getCoverImagePath(), entity.getStatus(), entity.getPublishedAt(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
