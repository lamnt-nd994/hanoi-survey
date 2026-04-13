package com.hanoisurvey.api.interfaces.rest.menus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuItemRequest(
        @NotNull Long menuId,
        Long parentId,
        @NotBlank String title,
        @NotBlank String itemType,
        Long refId,
        String url,
        String icon,
        Integer sortOrder,
        Boolean visible
) {
}
