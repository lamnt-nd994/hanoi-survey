package com.hanoisurvey.api.application.users;

import com.hanoisurvey.api.domain.users.AdminRole;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AdminRoleRepositoryPort {
    List<AdminRole> findAll();

    Optional<AdminRole> findById(Long id);

    List<AdminRole> findAllByIds(Set<Long> ids);

    boolean existsById(Long id);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    AdminRole save(AdminRole role);

    void deleteById(Long id);

    long countUsersByRoleId(Long roleId);
}
