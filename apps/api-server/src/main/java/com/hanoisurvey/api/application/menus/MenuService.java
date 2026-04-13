package com.hanoisurvey.api.application.menus;

import com.hanoisurvey.api.domain.menus.Menu;
import com.hanoisurvey.api.domain.menus.MenuItem;
import com.hanoisurvey.api.domain.shared.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepositoryPort menuRepository;
    private final MenuItemRepositoryPort menuItemRepository;

    public MenuService(MenuRepositoryPort menuRepository, MenuItemRepositoryPort menuItemRepository) {
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Menu getByCode(String code) {
        return menuRepository.findByCode(code).orElseThrow(() -> new NotFoundException("Không tìm thấy menu"));
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Menu createMenu(MenuCommand command) {
        return menuRepository.save(new Menu(null, command.code(), command.name(), command.position()));
    }

    public Menu updateMenu(Long id, MenuCommand command) {
        menuRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy menu"));
        return menuRepository.save(new Menu(id, command.code(), command.name(), command.position()));
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getItemsByMenuCode(String code) {
        Menu menu = getByCode(code);
        return menuItemRepository.findByMenuId(menu.id());
    }

    public MenuItem createItem(MenuItemCommand command) {
        Menu menu = menuRepository.findById(command.menuId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy menu"));
        if (command.parentId() != null) {
            menuItemRepository.findById(command.parentId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy parent menu item"));
        }
        return menuItemRepository.save(new MenuItem(
                null,
                menu.id(),
                command.parentId(),
                command.title(),
                command.itemType(),
                command.refId(),
                command.url(),
                command.icon(),
                command.sortOrder() == null ? 0 : command.sortOrder(),
                command.visible() == null || command.visible()
        ));
    }

    public MenuItem updateItem(Long id, MenuItemCommand command) {
        menuItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy menu item"));
        menuRepository.findById(command.menuId()).orElseThrow(() -> new NotFoundException("Không tìm thấy menu"));
        if (command.parentId() != null) {
            menuItemRepository.findById(command.parentId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy parent menu item"));
        }
        return menuItemRepository.save(new MenuItem(
                id,
                command.menuId(),
                command.parentId(),
                command.title(),
                command.itemType(),
                command.refId(),
                command.url(),
                command.icon(),
                command.sortOrder() == null ? 0 : command.sortOrder(),
                command.visible() == null || command.visible()
        ));
    }

    public void deleteItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy menu item");
        }
        menuItemRepository.deleteById(id);
    }
}
