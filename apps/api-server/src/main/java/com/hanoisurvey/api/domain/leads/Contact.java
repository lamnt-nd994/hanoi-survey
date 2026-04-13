package com.hanoisurvey.api.domain.leads;

import com.hanoisurvey.api.domain.shared.LeadStatus;

import java.time.LocalDateTime;

public record Contact(
        Long id,
        String fullName,
        String companyName,
        String phone,
        String email,
        String subject,
        String message,
        LeadStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
