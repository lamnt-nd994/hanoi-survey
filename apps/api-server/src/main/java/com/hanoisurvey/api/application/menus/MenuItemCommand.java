package com.hanoisurvey.api.application.menus;

public record MenuItemCommand(
        Long menuId,
        Long parentId,
        String title,
        String itemType,
        Long refId,
        String url,
        String icon,
        Integer sortOrder,
        Boolean visible
) {
}
