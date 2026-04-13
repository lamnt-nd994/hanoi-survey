package com.hanoisurvey.api.infrastructure.persistence.menus;

import com.hanoisurvey.api.application.menus.MenuRepositoryPort;
import com.hanoisurvey.api.domain.menus.Menu;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuRepositoryAdapter implements MenuRepositoryPort {

    private final MenuRepository repository;

    public MenuRepositoryAdapter(MenuRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Menu> findByCode(String code) {
        return repository.findByCode(code).map(this::toDomain);
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Menu> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Menu save(Menu menu) {
        MenuEntity entity = menu.id() == null ? new MenuEntity() : repository.findById(menu.id()).orElseGet(MenuEntity::new);
        entity.setId(menu.id());
        entity.setCode(menu.code());
        entity.setName(menu.name());
        entity.setPosition(menu.position());
        return toDomain(repository.save(entity));
    }

    private Menu toDomain(MenuEntity entity) {
        return new Menu(entity.getId(), entity.getCode(), entity.getName(), entity.getPosition());
    }
}
