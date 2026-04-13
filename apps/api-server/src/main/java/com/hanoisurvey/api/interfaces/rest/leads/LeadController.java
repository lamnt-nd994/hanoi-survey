package com.hanoisurvey.api.interfaces.rest.leads;

import com.hanoisurvey.api.application.leads.LeadService;
import com.hanoisurvey.api.application.leads.ContactCommand;
import com.hanoisurvey.api.interfaces.rest.leads.dto.ContactRequest;
import com.hanoisurvey.api.interfaces.rest.leads.dto.ContactResponse;
import com.hanoisurvey.api.interfaces.rest.leads.dto.UpdateLeadStatusRequest;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import com.hanoisurvey.api.interfaces.rest.shared.PagingMeta;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping("/api/public/v1/contacts")
    public ApiResponse<ContactResponse> createContact(@Valid @RequestBody ContactRequest request) {
        return ApiResponse.ok(ContactResponse.from(leadService.createContact(new ContactCommand(
                request.fullName(),
                request.companyName(),
                request.phone(),
                request.email(),
                request.subject(),
                request.message()
        ))));
    }

    @GetMapping("/api/admin/v1/contacts")
    public ApiResponse<List<ContactResponse>> contacts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = leadService.getContacts(PageRequest.of(page - 1, size));
        return ApiResponse.ok(result.getContent().stream().map(ContactResponse::from).toList(), PagingMeta.from(result));
    }

    @PutMapping("/api/admin/v1/contacts/{id}/status")
    public ApiResponse<ContactResponse> updateContactStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLeadStatusRequest request
    ) {
        return ApiResponse.ok(ContactResponse.from(leadService.updateContactStatus(id, request.status())));
    }
}
