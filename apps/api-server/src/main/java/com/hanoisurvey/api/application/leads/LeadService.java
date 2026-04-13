package com.hanoisurvey.api.application.leads;

import com.hanoisurvey.api.domain.leads.Contact;
import com.hanoisurvey.api.domain.shared.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LeadService {

    private final ContactRepositoryPort contactRepository;

    public LeadService(
            ContactRepositoryPort contactRepository
    ) {
        this.contactRepository = contactRepository;
    }

    public Contact createContact(ContactCommand command) {
        return contactRepository.save(new Contact(null, command.fullName(), command.companyName(), command.phone(), command.email(), command.subject(), command.message(), com.hanoisurvey.api.domain.shared.LeadStatus.NEW, null, null));
    }

    public Page<Contact> getContacts(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    public Contact updateContactStatus(Long id, com.hanoisurvey.api.domain.shared.LeadStatus status) {
        contactRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy liên hệ"));
        return contactRepository.updateStatus(id, status);
    }
}
