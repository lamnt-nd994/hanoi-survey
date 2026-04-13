package com.hanoisurvey.api.infrastructure.persistence.auth;

import com.hanoisurvey.api.application.users.AdminRoleRepositoryPort;
import com.hanoisurvey.api.domain.users.AdminRole;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class AdminRoleRepositoryAdapter implements AdminRoleRepositoryPort {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public AdminRoleRepositoryAdapter(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AdminRole> findAll() {
        return roleRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<AdminRole> findById(Long id) {
        return roleRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<AdminRole> findAllByIds(Set<Long> ids) {
        return roleRepository.findAllById(ids).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return roleRepository.existsByCode(code);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, Long id) {
        return roleRepository.existsByCodeAndIdNot(code, id);
    }

    @Override
    public AdminRole save(AdminRole role) {
        RoleEntity entity = role.id() == null ? new RoleEntity() : roleRepository.findById(role.id()).orElseGet(RoleEntity::new);
        entity.setId(role.id());
        entity.setCode(role.code());
        entity.setName(role.name());
        entity.setAdminAccess(Boolean.TRUE.equals(role.adminAccess()));
        return toDomain(roleRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public long countUsersByRoleId(Long roleId) {
        return userRepository.countByRoles_Id(roleId);
    }

    private AdminRole toDomain(RoleEntity entity) {
        return new AdminRole(entity.getId(), entity.getCode(), entity.getName(), entity.getAdminAccess(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
