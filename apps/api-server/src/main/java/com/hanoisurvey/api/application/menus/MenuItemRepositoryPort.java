package com.hanoisurvey.api.application.menus;

import com.hanoisurvey.api.domain.menus.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepositoryPort {
    List<MenuItem> findByMenuId(Long menuId);

    Optional<MenuItem> findById(Long id);

    boolean existsById(Long id);

    MenuItem save(MenuItem item);

    void deleteById(Long id);
}
