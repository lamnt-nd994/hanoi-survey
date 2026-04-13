package com.hanoisurvey.api.interfaces.rest.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record AdminUserUpsertRequest(
        @NotBlank String username,
        String password,
        @NotBlank String fullName,
        String email,
        @NotNull Boolean active,
        @NotEmpty Set<Long> roleIds
) {
}
