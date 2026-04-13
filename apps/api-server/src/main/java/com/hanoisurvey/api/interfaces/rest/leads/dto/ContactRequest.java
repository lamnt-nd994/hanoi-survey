package com.hanoisurvey.api.interfaces.rest.leads.dto;

import jakarta.validation.constraints.NotBlank;

public record ContactRequest(
        @NotBlank String fullName,
        String companyName,
        @NotBlank String phone,
        String email,
        String subject,
        @NotBlank String message
) {
}
