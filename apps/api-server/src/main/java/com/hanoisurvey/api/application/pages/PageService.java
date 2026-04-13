package com.hanoisurvey.api.application.pages;

import com.hanoisurvey.api.domain.pages.Page;
import com.hanoisurvey.api.domain.shared.ContentStatus;
import com.hanoisurvey.api.domain.shared.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PageService {

    private final PageRepositoryPort repository;

    public PageService(PageRepositoryPort repository) {
        this.repository = repository;
    }

    public Page getPublicBySlug(String slug) {
        return repository.findPublishedBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy trang"));
    }

    public org.springframework.data.domain.Page<Page> getAdminPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page getAdminById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy trang"));
    }

    public Page create(PageCommand command) {
        return save(toDomain(command, null));
    }

    public Page update(Long id, PageCommand command) {
        return save(toDomain(command, id));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy trang");
        }
        repository.deleteById(id);
    }

    private Page save(Page page) {
        try {
            return repository.save(page);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException("Slug đã tồn tại");
        }
    }

    private Page toDomain(PageCommand command, Long id) {
        return new Page(
                id,
                command.title(),
                command.slug(),
                command.summary(),
                command.content(),
                command.templateCode(),
                command.contentJson(),
                command.status(),
                command.status() == ContentStatus.PUBLISHED ? LocalDateTime.now() : null,
                null,
                null
        );
    }
}
