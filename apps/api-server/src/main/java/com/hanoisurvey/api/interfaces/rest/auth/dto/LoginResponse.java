package com.hanoisurvey.api.interfaces.rest.auth.dto;

public record LoginResponse(String accessToken, AuthenticatedUserResponse user) {
}
