package com.hanoisurvey.api.infrastructure.persistence.pages;

import com.hanoisurvey.api.application.pages.PageRepositoryPort;
import com.hanoisurvey.api.domain.pages.Page;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PageRepositoryAdapter implements PageRepositoryPort {

    private final PageRepository repository;

    public PageRepositoryAdapter(PageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Page> findPublishedBySlug(String slug) {
        return repository.findBySlugAndStatus(slug, ContentStatus.PUBLISHED).map(this::toDomain);
    }

    @Override
    public org.springframework.data.domain.Page<Page> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Optional<Page> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Page save(Page page) {
        PageEntity entity = page.id() == null ? new PageEntity() : repository.findById(page.id()).orElseGet(PageEntity::new);
        entity.setId(page.id());
        entity.setTitle(page.title());
        entity.setSlug(page.slug());
        entity.setSummary(page.summary());
        entity.setContent(page.content());
        entity.setTemplateCode(page.templateCode());
        entity.setContentJson(page.contentJson());
        entity.setStatus(page.status());
        entity.setPublishedAt(page.publishedAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Page toDomain(PageEntity entity) {
        return new Page(entity.getId(), entity.getTitle(), entity.getSlug(), entity.getSummary(), entity.getContent(), entity.getTemplateCode(), entity.getContentJson(), entity.getStatus(), entity.getPublishedAt(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
