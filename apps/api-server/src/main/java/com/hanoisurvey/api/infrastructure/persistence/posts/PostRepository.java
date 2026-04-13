package com.hanoisurvey.api.infrastructure.persistence.posts;

import com.hanoisurvey.api.domain.shared.ContentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Override
    @EntityGraph(attributePaths = "category")
    Optional<PostEntity> findById(Long id);

    @EntityGraph(attributePaths = "category")
    Optional<PostEntity> findBySlugAndStatus(String slug, ContentStatus status);

    @EntityGraph(attributePaths = "category")
    Page<PostEntity> findByStatus(ContentStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<PostEntity> findByStatusAndCategory_Slug(ContentStatus status, String categorySlug, Pageable pageable);

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p FROM PostEntity p WHERE (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<PostEntity> findByKeywordAndCategoryId(@Param("keyword") String keyword, @Param("categoryId") Long categoryId, Pageable pageable);
}
