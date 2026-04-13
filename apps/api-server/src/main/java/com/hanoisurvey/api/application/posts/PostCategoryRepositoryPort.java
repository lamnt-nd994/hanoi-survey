package com.hanoisurvey.api.application.posts;

import com.hanoisurvey.api.domain.posts.PostCategory;

import java.util.List;
import java.util.Optional;

public interface PostCategoryRepositoryPort {
    List<PostCategory> findAll();

    Optional<PostCategory> findById(Long id);

    boolean existsById(Long id);

    PostCategory save(PostCategory category);

    void deleteById(Long id);
}
