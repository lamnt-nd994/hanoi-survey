package com.hanoisurvey.api.infrastructure.persistence.leads;

import com.hanoisurvey.api.application.leads.ContactRepositoryPort;
import com.hanoisurvey.api.domain.leads.Contact;
import com.hanoisurvey.api.domain.shared.LeadStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ContactRepositoryAdapter implements ContactRepositoryPort {

    private final ContactRepository repository;

    public ContactRepositoryAdapter(ContactRepository repository) {
        this.repository = repository;
    }

    @Override
    public Contact save(Contact contact) {
        ContactEntity entity = contact.id() == null ? new ContactEntity() : repository.findById(contact.id()).orElseGet(ContactEntity::new);
        entity.setId(contact.id());
        entity.setFullName(contact.fullName());
        entity.setCompanyName(contact.companyName());
        entity.setPhone(contact.phone());
        entity.setEmail(contact.email());
        entity.setSubject(contact.subject());
        entity.setMessage(contact.message());
        entity.setStatus(contact.status());
        return toDomain(repository.save(entity));
    }

    @Override
    public org.springframework.data.domain.Page<Contact> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Contact updateStatus(Long id, LeadStatus status) {
        ContactEntity entity = repository.findById(id).orElseThrow();
        entity.setStatus(status);
        return toDomain(repository.save(entity));
    }

    private Contact toDomain(ContactEntity entity) {
        return new Contact(entity.getId(), entity.getFullName(), entity.getCompanyName(), entity.getPhone(), entity.getEmail(), entity.getSubject(), entity.getMessage(), entity.getStatus(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
