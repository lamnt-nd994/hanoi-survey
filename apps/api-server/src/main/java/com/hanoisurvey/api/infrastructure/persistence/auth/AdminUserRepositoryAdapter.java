package com.hanoisurvey.api.infrastructure.persistence.auth;

import com.hanoisurvey.api.application.users.AdminUserRepositoryPort;
import com.hanoisurvey.api.domain.users.AdminRole;
import com.hanoisurvey.api.domain.users.AdminUser;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class AdminUserRepositoryAdapter implements AdminUserRepositoryPort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminUserRepositoryAdapter(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<AdminUser> findAll(String keyword, Long roleId, Boolean active, Pageable pageable) {
        Specification<UserEntity> specification = Specification.where(null);

        if (keyword != null && !keyword.trim().isEmpty()) {
            String likeValue = "%" + keyword.trim().toLowerCase() + "%";
            specification = specification.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("username")), likeValue),
                    cb.like(cb.lower(root.get("fullName")), likeValue),
                    cb.like(cb.lower(root.get("email")), likeValue)
            ));
        }

        if (roleId != null) {
            specification = specification.and((root, query, cb) -> {
                query.distinct(true);
                return cb.equal(root.join("roles", JoinType.INNER).get("id"), roleId);
            });
        }

        if (active != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("active"), active));
        }

        return userRepository.findAll(specification, pageable).map(this::toDomain);
    }

    @Override
    public Optional<AdminUser> findById(Long id) {
        return userRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByUsernameAndIdNot(String username, Long id) {
        return userRepository.existsByUsernameAndIdNot(username, id);
    }

    @Override
    public Optional<String> getPasswordHashById(Long id) {
        return userRepository.findById(id).map(UserEntity::getPasswordHash);
    }

    @Override
    public AdminUser save(AdminUser user, Set<Long> roleIds, String passwordHash) {
        UserEntity entity = user.id() == null ? new UserEntity() : userRepository.findById(user.id()).orElseGet(UserEntity::new);
        entity.setId(user.id());
        entity.setUsername(user.username());
        entity.setFullName(user.fullName());
        entity.setEmail(user.email());
        entity.setActive(Boolean.TRUE.equals(user.active()));
        if (passwordHash != null) {
            entity.setPasswordHash(passwordHash);
        }
        entity.setRoles(new LinkedHashSet<>(roleRepository.findAllById(roleIds)));
        return toDomain(userRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private AdminUser toDomain(UserEntity entity) {
        return new AdminUser(
                entity.getId(),
                entity.getUsername(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getActive(),
                entity.getRoles().stream().map(role -> new AdminRole(role.getId(), role.getCode(), role.getName(), role.getAdminAccess(), role.getCreatedAt(), role.getUpdatedAt())).collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
