package com.hanoisurvey.api.application.posts;

import com.hanoisurvey.api.domain.posts.Post;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepositoryPort {
    Optional<Post> findPublishedBySlug(String slug);

    org.springframework.data.domain.Page<Post> findPublished(String categorySlug, Pageable pageable);

    org.springframework.data.domain.Page<Post> findAll(Pageable pageable);

    org.springframework.data.domain.Page<Post> findAllFiltered(String keyword, Long categoryId, Pageable pageable);

    Optional<Post> findById(Long id);

    boolean existsById(Long id);

    Post save(Post post);

    void deleteById(Long id);
}
