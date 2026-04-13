package com.hanoisurvey.api.domain.menus;

public record MenuItem(
        Long id,
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
