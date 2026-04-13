package com.hanoisurvey.api.interfaces.rest.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminRoleUpsertRequest(
        @NotBlank String code,
        @NotBlank String name,
        @NotNull Boolean adminAccess
) {
}
