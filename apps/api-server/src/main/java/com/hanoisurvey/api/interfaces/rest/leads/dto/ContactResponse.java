package com.hanoisurvey.api.interfaces.rest.leads.dto;

import com.hanoisurvey.api.domain.leads.Contact;
import com.hanoisurvey.api.domain.shared.LeadStatus;

import java.time.LocalDateTime;

public record ContactResponse(
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
    public static ContactResponse from(Contact contact) {
        return new ContactResponse(
                contact.id(),
                contact.fullName(),
                contact.companyName(),
                contact.phone(),
                contact.email(),
                contact.subject(),
                contact.message(),
                contact.status(),
                contact.createdAt(),
                contact.updatedAt()
        );
    }
}
