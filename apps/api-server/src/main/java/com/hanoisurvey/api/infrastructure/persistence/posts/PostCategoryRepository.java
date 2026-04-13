package com.hanoisurvey.api.infrastructure.persistence.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategoryEntity, Long> {
}
