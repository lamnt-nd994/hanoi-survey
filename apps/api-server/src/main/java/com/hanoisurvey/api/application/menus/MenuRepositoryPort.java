package com.hanoisurvey.api.application.menus;

import com.hanoisurvey.api.domain.menus.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepositoryPort {
    Optional<Menu> findByCode(String code);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    Menu save(Menu menu);
}
