package com.hanoisurvey.api.interfaces.rest.menus.dto;

import com.hanoisurvey.api.domain.menus.MenuItem;

public record MenuItemResponse(
        Long id,
        Long menuId,
        Long parentId,
        String title,
        String itemType,
        Long refId,
        String url,
        String resolvedUrl,
        String icon,
        Integer sortOrder,
        Boolean visible
) {
    public static MenuItemResponse from(MenuItem item) {
        return from(item, item.url());
    }

    public static MenuItemResponse from(MenuItem item, String resolvedUrl) {
        return new MenuItemResponse(
                item.id(),
                item.menuId(),
                item.parentId(),
                item.title(),
                item.itemType(),
                item.refId(),
                item.url(),
                resolvedUrl,
                item.icon(),
                item.sortOrder(),
                item.visible()
        );
    }
}
