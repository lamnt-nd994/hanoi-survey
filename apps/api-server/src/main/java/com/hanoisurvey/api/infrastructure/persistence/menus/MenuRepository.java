package com.hanoisurvey.api.infrastructure.persistence.menus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    Optional<MenuEntity> findByCode(String code);
}
