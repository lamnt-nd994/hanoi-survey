package com.hanoisurvey.api.infrastructure.persistence.posts;

import com.hanoisurvey.api.application.posts.PostCategoryRepositoryPort;
import com.hanoisurvey.api.domain.posts.PostCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostCategoryRepositoryAdapter implements PostCategoryRepositoryPort {

    private final PostCategoryRepository repository;

    public PostCategoryRepositoryAdapter(PostCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PostCategory> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<PostCategory> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public PostCategory save(PostCategory category) {
        PostCategoryEntity entity = category.id() == null ? new PostCategoryEntity() : repository.findById(category.id()).orElseGet(PostCategoryEntity::new);
        entity.setId(category.id());
        entity.setName(category.name());
        entity.setSlug(category.slug());
        entity.setParent(category.parentId() == null ? null : repository.findById(category.parentId()).orElseThrow());
        entity.setSortOrder(category.sortOrder());
        entity.setActive(category.active());
        return toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private PostCategory toDomain(PostCategoryEntity entity) {
        return new PostCategory(entity.getId(), entity.getName(), entity.getSlug(), entity.getParent() == null ? null : entity.getParent().getId(), entity.getSortOrder(), entity.getActive());
    }
}
