package com.hanoisurvey.api.application.users;

import com.hanoisurvey.api.domain.users.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface AdminUserRepositoryPort {
    Page<AdminUser> findAll(String keyword, Long roleId, Boolean active, Pageable pageable);

    Optional<AdminUser> findById(Long id);

    boolean existsById(Long id);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    Optional<String> getPasswordHashById(Long id);

    AdminUser save(AdminUser user, Set<Long> roleIds, String passwordHash);

    void deleteById(Long id);
}
