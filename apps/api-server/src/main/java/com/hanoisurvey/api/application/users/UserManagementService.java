package com.hanoisurvey.api.application.users;

import com.hanoisurvey.api.domain.shared.NotFoundException;
import com.hanoisurvey.api.domain.users.AdminRole;
import com.hanoisurvey.api.domain.users.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class UserManagementService {

    private final AdminUserRepositoryPort userRepository;
    private final AdminRoleRepositoryPort roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementService(AdminUserRepositoryPort userRepository, AdminRoleRepositoryPort roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<AdminRole> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<AdminUser> getUsers(String keyword, Long roleId, Boolean active, Pageable pageable) {
        return userRepository.findAll(keyword, roleId, active, pageable);
    }

    @Transactional(readOnly = true)
    public AdminUser getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
    }

    @Transactional
    public AdminRole createRole(AdminRoleCommand command) {
        String code = normalizeRoleCode(command.code());
        if (roleRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Mã quyền đã tồn tại");
        }
        return roleRepository.save(new AdminRole(null, code, normalizeRequired(command.name(), "Tên quyền là bắt buộc"), command.adminAccess() != null && command.adminAccess(), null, null));
    }

    @Transactional
    public AdminRole updateRole(Long id, AdminRoleCommand command) {
        roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy quyền"));
        String code = normalizeRoleCode(command.code());
        if (roleRepository.existsByCodeAndIdNot(code, id)) {
            throw new IllegalArgumentException("Mã quyền đã tồn tại");
        }
        return roleRepository.save(new AdminRole(id, code, normalizeRequired(command.name(), "Tên quyền là bắt buộc"), command.adminAccess() != null && command.adminAccess(), null, null));
    }

    @Transactional
    public void deleteRole(Long id) {
        AdminRole role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy quyền"));
        if (roleRepository.countUsersByRoleId(id) > 0) {
            throw new IllegalArgumentException("Không thể xóa quyền đang được gán cho người dùng");
        }
        if ("ROLE_ADMIN".equals(role.code())) {
            throw new IllegalArgumentException("Không thể xóa quyền ROLE_ADMIN");
        }
        roleRepository.deleteById(id);
    }

    @Transactional
    public AdminUser createUser(AdminUserCommand command) {
        String username = normalizeRequired(command.username(), "Tên đăng nhập là bắt buộc");
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        String rawPassword = normalizeRequired(command.password(), "Mật khẩu là bắt buộc");
        Set<Long> roleIds = validateRoleIds(command.roleIds());
        return userRepository.save(
                new AdminUser(null, username, normalizeRequired(command.fullName(), "Họ tên là bắt buộc"), normalizeNullable(command.email()), command.active() == null || command.active(), Set.of(), null, null),
                roleIds,
                passwordEncoder.encode(rawPassword)
        );
    }

    @Transactional
    public AdminUser updateUser(Long id, AdminUserCommand command) {
        AdminUser existing = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        String username = normalizeRequired(command.username(), "Tên đăng nhập là bắt buộc");
        if (userRepository.existsByUsernameAndIdNot(username, id)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        Set<Long> roleIds = validateRoleIds(command.roleIds());
        String passwordHash = existing == null ? null : null;
        if (command.password() != null && !command.password().trim().isEmpty()) {
            passwordHash = passwordEncoder.encode(command.password().trim());
        }
        return userRepository.save(
                new AdminUser(id, username, normalizeRequired(command.fullName(), "Họ tên là bắt buộc"), normalizeNullable(command.email()), command.active() == null || command.active(), Set.of(), existing.createdAt(), existing.updatedAt()),
                roleIds,
                passwordHash
        );
    }

    @Transactional
    public void deleteUser(Long id, Long currentUserId) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Không tìm thấy người dùng");
        }
        if (currentUserId != null && currentUserId.equals(id)) {
            throw new IllegalArgumentException("Không thể xóa chính tài khoản đang đăng nhập");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void resetPassword(Long id, AdminResetPasswordCommand command, Long currentUserId) {
        AdminUser user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));
        String password = normalizeRequired(command.password(), "Mật khẩu mới là bắt buộc");
        userRepository.save(user, user.roles().stream().map(AdminRole::id).collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)), passwordEncoder.encode(password));
    }

    private Set<Long> validateRoleIds(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Người dùng phải có ít nhất một quyền");
        }
        Set<Long> normalizedIds = roleIds.stream().filter(id -> id != null && id > 0).collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
        if (normalizedIds.isEmpty()) {
            throw new IllegalArgumentException("Người dùng phải có ít nhất một quyền");
        }
        List<AdminRole> roles = roleRepository.findAllByIds(normalizedIds);
        if (roles.size() != normalizedIds.size()) {
            throw new IllegalArgumentException("Có quyền không tồn tại trong hệ thống");
        }
        return normalizedIds;
    }

    private String normalizeRoleCode(String value) {
        String normalized = normalizeRequired(value, "Mã quyền là bắt buộc")
                .replace('-', '_')
                .replace(' ', '_')
                .toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z0-9_]", "")
                .replaceAll("_+", "_")
                .replaceAll("^_+|_+$", "");
        if (!normalized.startsWith("ROLE_")) {
            normalized = "ROLE_" + normalized;
        }
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Mã quyền không hợp lệ");
        }
        return normalized;
    }

    private String normalizeRequired(String value, String message) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    private String normalizeNullable(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
