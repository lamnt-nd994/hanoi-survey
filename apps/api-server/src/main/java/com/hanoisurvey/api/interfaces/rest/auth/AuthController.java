package com.hanoisurvey.api.interfaces.rest.auth;

import com.hanoisurvey.api.application.auth.AuthService;
import com.hanoisurvey.api.interfaces.rest.auth.dto.AuthenticatedUserResponse;
import com.hanoisurvey.api.interfaces.rest.auth.dto.LoginRequest;
import com.hanoisurvey.api.interfaces.rest.auth.dto.LoginResponse;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthService.AuthResult result = authService.login(request.username(), request.password());
        return ApiResponse.ok(new LoginResponse(result.accessToken(), AuthenticatedUserResponse.from(result.user())));
    }

    @GetMapping("/me")
    public ApiResponse<AuthenticatedUserResponse> me() {
        return ApiResponse.ok(AuthenticatedUserResponse.from(authService.me()));
    }
}
