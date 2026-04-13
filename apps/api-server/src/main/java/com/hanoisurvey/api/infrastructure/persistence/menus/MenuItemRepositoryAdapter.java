package com.hanoisurvey.api.infrastructure.persistence.menus;

import com.hanoisurvey.api.application.menus.MenuItemRepositoryPort;
import com.hanoisurvey.api.domain.menus.MenuItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuItemRepositoryAdapter implements MenuItemRepositoryPort {

    private final MenuItemRepository repository;
    private final MenuRepository menuRepository;

    public MenuItemRepositoryAdapter(MenuItemRepository repository, MenuRepository menuRepository) {
        this.repository = repository;
        this.menuRepository = menuRepository;
    }

    @Override
    public List<MenuItem> findByMenuId(Long menuId) {
        return repository.findByMenu_IdOrderBySortOrderAsc(menuId).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public MenuItem save(MenuItem item) {
        MenuItemEntity entity = item.id() == null ? new MenuItemEntity() : repository.findById(item.id()).orElseGet(MenuItemEntity::new);
        entity.setId(item.id());
        entity.setMenu(menuRepository.findById(item.menuId()).orElseThrow());
        entity.setParent(item.parentId() == null ? null : repository.findById(item.parentId()).orElseThrow());
        entity.setTitle(item.title());
        entity.setItemType(item.itemType());
        entity.setRefId(item.refId());
        entity.setUrl(item.url());
        entity.setIcon(item.icon());
        entity.setSortOrder(item.sortOrder());
        entity.setVisible(item.visible());
        return toDomain(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private MenuItem toDomain(MenuItemEntity entity) {
        return new MenuItem(entity.getId(), entity.getMenu().getId(), entity.getParent() == null ? null : entity.getParent().getId(), entity.getTitle(), entity.getItemType(), entity.getRefId(), entity.getUrl(), entity.getIcon(), entity.getSortOrder(), entity.getVisible());
    }
}
