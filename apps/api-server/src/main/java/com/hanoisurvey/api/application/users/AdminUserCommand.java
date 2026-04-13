package com.hanoisurvey.api.application.users;

import java.util.Set;

public record AdminUserCommand(
        String username,
        String password,
        String fullName,
        String email,
        Boolean active,
        Set<Long> roleIds
) {
}
