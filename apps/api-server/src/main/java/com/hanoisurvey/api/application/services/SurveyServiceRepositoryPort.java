package com.hanoisurvey.api.application.services;

import com.hanoisurvey.api.domain.services.SurveyService;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SurveyServiceRepositoryPort {
    Optional<SurveyService> findPublishedBySlug(String slug);

    org.springframework.data.domain.Page<SurveyService> findPublished(String categorySlug, Pageable pageable);

    org.springframework.data.domain.Page<SurveyService> findAll(Pageable pageable);

    Optional<SurveyService> findById(Long id);

    boolean existsById(Long id);

    SurveyService save(SurveyService surveyService);

    void deleteById(Long id);
}
