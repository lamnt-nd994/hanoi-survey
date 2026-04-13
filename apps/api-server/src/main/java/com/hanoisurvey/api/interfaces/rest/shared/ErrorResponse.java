package com.hanoisurvey.api.interfaces.rest.shared;

import java.util.List;

public record ErrorResponse(boolean success, ErrorBody error, String traceId) {

    public record ErrorBody(String code, String message, List<FieldErrorItem> details) {
    }

    public record FieldErrorItem(String field, String message) {
    }
}
