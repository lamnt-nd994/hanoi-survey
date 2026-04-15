package com.hanoisurvey.api.interfaces.rest.users.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateMyProfileRequest(
        @NotBlank String fullName,
        String email
) {
}
