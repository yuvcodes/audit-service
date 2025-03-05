package com.microservices.auditservice.bootstrap;

import com.microservices.auditservice.entity.Users;
import com.microservices.auditservice.entity.auth.Role;
import com.microservices.auditservice.model.enums.RoleEnum;
import com.microservices.auditservice.model.users.RegisterUser;
import com.microservices.auditservice.repository.RoleRepository;
import com.microservices.auditservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    String adminUserName;
    @Value("${admin.password}")
    String adminPassword;


    public AdminSeeder(
            RoleRepository roleRepository,
            UserRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    private void createSuperAdministrator() {
        RegisterUser registerUserInput = RegisterUser.builder()
                .userName(adminUserName)
                .password(adminPassword)
                .build();

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
        Optional<Users> optionalUser = userRepository.findByUserName(registerUserInput.getUserName());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        Users user = Users.builder()
                .userName(registerUserInput.getUserName())
                .userId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(optionalRole.get())
                .password(passwordEncoder.encode(registerUserInput.getPassword())).build();

        userRepository.save(user);
    }
}
