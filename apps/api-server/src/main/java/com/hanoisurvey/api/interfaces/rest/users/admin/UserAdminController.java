package com.hanoisurvey.api.interfaces.rest.users.admin;

import com.hanoisurvey.api.application.auth.AuthService;
import com.hanoisurvey.api.application.users.AdminRoleCommand;
import com.hanoisurvey.api.application.users.AdminResetPasswordCommand;
import com.hanoisurvey.api.application.users.AdminUserCommand;
import com.hanoisurvey.api.application.users.UserManagementService;
import com.hanoisurvey.api.interfaces.rest.users.dto.AdminResetPasswordRequest;
import com.hanoisurvey.api.interfaces.rest.shared.ApiResponse;
import com.hanoisurvey.api.interfaces.rest.shared.PagingMeta;
import com.hanoisurvey.api.interfaces.rest.users.dto.AdminRoleResponse;
import com.hanoisurvey.api.interfaces.rest.users.dto.AdminRoleUpsertRequest;
import com.hanoisurvey.api.interfaces.rest.users.dto.AdminUserResponse;
import com.hanoisurvey.api.interfaces.rest.users.dto.AdminUserUpsertRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/admin/v1")
public class UserAdminController {

    private final UserManagementService userManagementService;
    private final AuthService authService;

    public UserAdminController(UserManagementService userManagementService, AuthService authService) {
        this.userManagementService = userManagementService;
        this.authService = authService;
    }

    @GetMapping("/roles")
    public ApiResponse<List<AdminRoleResponse>> roles() {
        return ApiResponse.ok(userManagementService.getAllRoles().stream().map(AdminRoleResponse::from).toList());
    }

    @PostMapping("/roles")
    public ApiResponse<AdminRoleResponse> createRole(@Valid @RequestBody AdminRoleUpsertRequest request) {
        return ApiResponse.ok(AdminRoleResponse.from(userManagementService.createRole(new AdminRoleCommand(
                request.code(),
                request.name(),
                request.adminAccess()
        ))));
    }

    @PutMapping("/roles/{id}")
    public ApiResponse<AdminRoleResponse> updateRole(@PathVariable Long id, @Valid @RequestBody AdminRoleUpsertRequest request) {
        return ApiResponse.ok(AdminRoleResponse.from(userManagementService.updateRole(id, new AdminRoleCommand(
                request.code(),
                request.name(),
                request.adminAccess()
        ))));
    }

    @DeleteMapping("/roles/{id}")
    public ApiResponse<String> deleteRole(@PathVariable Long id) {
        userManagementService.deleteRole(id);
        return ApiResponse.ok("Deleted");
    }

    @GetMapping("/users")
    public ApiResponse<List<AdminUserResponse>> users(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Boolean active
    ) {
        var result = userManagementService.getUsers(keyword, roleId, active, PageRequest.of(page - 1, size));
        return ApiResponse.ok(result.getContent().stream().map(AdminUserResponse::from).toList(), PagingMeta.from(result));
    }

    @GetMapping("/users/{id}")
    public ApiResponse<AdminUserResponse> userDetail(@PathVariable Long id) {
        return ApiResponse.ok(AdminUserResponse.from(userManagementService.getUserById(id)));
    }

    @PostMapping("/users")
    public ApiResponse<AdminUserResponse> createUser(@Valid @RequestBody AdminUserUpsertRequest request) {
        return ApiResponse.ok(AdminUserResponse.from(userManagementService.createUser(toCommand(request))));
    }

    @PutMapping("/users/{id}")
    public ApiResponse<AdminUserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody AdminUserUpsertRequest request) {
        return ApiResponse.ok(AdminUserResponse.from(userManagementService.updateUser(id, toCommand(request))));
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        Long currentUserId = authService.me() == null ? null : authService.me().id();
        userManagementService.deleteUser(id, currentUserId);
        return ApiResponse.ok("Deleted");
    }

    @PostMapping("/users/{id}/reset-password")
    public ApiResponse<String> resetPassword(@PathVariable Long id, @Valid @RequestBody AdminResetPasswordRequest request) {
        Long currentUserId = authService.me() == null ? null : authService.me().id();
        userManagementService.resetPassword(id, new AdminResetPasswordCommand(request.password()), currentUserId);
        return ApiResponse.ok("Password reset");
    }

    private AdminUserCommand toCommand(AdminUserUpsertRequest request) {
        return new AdminUserCommand(
                request.username(),
                request.password(),
                request.fullName(),
                request.email(),
                request.active(),
                request.roleIds()
        );
    }
}
