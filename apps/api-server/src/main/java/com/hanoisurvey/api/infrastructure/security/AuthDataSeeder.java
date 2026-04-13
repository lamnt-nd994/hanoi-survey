package com.hanoisurvey.api.infrastructure.security;

import com.hanoisurvey.api.domain.auth.RoleCode;
import com.hanoisurvey.api.infrastructure.persistence.auth.RoleEntity;
import com.hanoisurvey.api.infrastructure.persistence.auth.RoleRepository;
import com.hanoisurvey.api.infrastructure.persistence.auth.UserEntity;
import com.hanoisurvey.api.infrastructure.persistence.auth.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class AuthDataSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthDataSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Arrays.stream(RoleCode.values()).forEach(roleCode -> roleRepository.findByCode(roleCode.name()).orElseGet(() -> {
            RoleEntity role = new RoleEntity();
            role.setCode(roleCode.name());
            role.setName(roleCode.name());
            role.setAdminAccess(true);
            return roleRepository.save(role);
        }));

        userRepository.findByUsername("admin").orElseGet(() -> {
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            user.setPasswordHash(passwordEncoder.encode("Admin@123"));
            user.setFullName("System Administrator");
            user.setEmail("admin@hanoisurvey.vn");
            user.setActive(true);
            user.setRoles(new HashSet<>(roleRepository.findAll()));
            return userRepository.save(user);
        });
    }
}
