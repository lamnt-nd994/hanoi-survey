package com.hanoisurvey.api.interfaces.rest.menus.dto;

import jakarta.validation.constraints.NotBlank;

public record MenuRequest(
        @NotBlank String code,
        @NotBlank String name,
        @NotBlank String position
) {
}
