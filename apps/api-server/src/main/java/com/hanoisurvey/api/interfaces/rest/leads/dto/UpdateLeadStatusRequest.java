package com.hanoisurvey.api.interfaces.rest.leads.dto;

import com.hanoisurvey.api.domain.shared.LeadStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateLeadStatusRequest(@NotNull LeadStatus status) {
}
