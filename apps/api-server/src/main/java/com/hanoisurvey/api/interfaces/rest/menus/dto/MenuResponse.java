package com.hanoisurvey.api.interfaces.rest.menus.dto;

import com.hanoisurvey.api.domain.menus.Menu;

public record MenuResponse(Long id, String code, String name, String position) {

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.id(), menu.code(), menu.name(), menu.position());
    }
}
