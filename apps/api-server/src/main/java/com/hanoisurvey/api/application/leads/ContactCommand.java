package com.hanoisurvey.api.application.leads;

public record ContactCommand(String fullName, String companyName, String phone, String email, String subject, String message) {
}
