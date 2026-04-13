package com.hanoisurvey.api.infrastructure.persistence.menus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {
    List<MenuItemEntity> findByMenu_IdOrderBySortOrderAsc(Long menuId);
}
