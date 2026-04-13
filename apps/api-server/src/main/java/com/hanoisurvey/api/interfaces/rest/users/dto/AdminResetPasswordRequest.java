package com.hanoisurvey.api.interfaces.rest.users.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminResetPasswordRequest(
        @NotBlank String password
) {
}
