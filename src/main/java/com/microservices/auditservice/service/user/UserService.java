package com.microservices.auditservice.service.user;

import com.microservices.auditservice.entity.Users;
import com.microservices.auditservice.entity.auth.Role;
import com.microservices.auditservice.model.enums.RoleEnum;
import com.microservices.auditservice.model.users.RegisterUser;
import com.microservices.auditservice.repository.RoleRepository;
import com.microservices.auditservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Users createAdministrator(RegisterUser registerUserInput) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) {
            return null;
        }

        Users user = Users.builder()
                .userName(registerUserInput.getUserName())
                .userId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(optionalRole.get())
                .password(passwordEncoder.encode(registerUserInput.getPassword())).build();

        return userRepository.save(user);
    }
}
