package com.hanoisurvey.api.interfaces.rest.users.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeMyPasswordRequest(
        @NotBlank String currentPassword,
        @NotBlank String newPassword
) {
}
