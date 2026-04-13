package com.hanoisurvey.api.application.pages;

import com.hanoisurvey.api.domain.pages.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PageRepositoryPort {
    Optional<Page> findPublishedBySlug(String slug);

    org.springframework.data.domain.Page<Page> findAll(Pageable pageable);

    Optional<Page> findById(Long id);

    boolean existsById(Long id);

    Page save(Page page);

    void deleteById(Long id);
}
