package com.hanoisurvey.api.application.leads;

import com.hanoisurvey.api.domain.leads.Contact;
import com.hanoisurvey.api.domain.shared.LeadStatus;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContactRepositoryPort {
    Contact save(Contact contact);

    org.springframework.data.domain.Page<Contact> findAll(Pageable pageable);

    Optional<Contact> findById(Long id);

    Contact updateStatus(Long id, LeadStatus status);
}
