package com.hanoisurvey.api.application.users;

public record AdminRoleCommand(
        String code,
        String name,
        Boolean adminAccess
) {
}
