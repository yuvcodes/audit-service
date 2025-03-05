package com.microservices.auditservice.service.auth;

import com.microservices.auditservice.entity.Users;
import com.microservices.auditservice.entity.auth.Role;
import com.microservices.auditservice.model.enums.RoleEnum;
import com.microservices.auditservice.model.users.LoginUser;
import com.microservices.auditservice.model.users.RegisterUser;
import com.microservices.auditservice.repository.RoleRepository;
import com.microservices.auditservice.repository.UserRepository;
import com.microservices.auditservice.util.JsonMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JsonMapperUtil jsonMapperUtil;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users signup(RegisterUser registerUserInput) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) return null;

        Users user = Users.builder()
                .userName(registerUserInput.getUserName())
                .userId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(optionalRole.get())
                .password(passwordEncoder.encode(registerUserInput.getPassword())).build();

        return userRepository.save(user);
    }

    public Users authenticate(LoginUser loginUserInput) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserInput.getUserName(),
                        loginUserInput.getPassword()
                )
        );

        return userRepository.findByUserName(loginUserInput.getUserName())
                .orElseThrow();
    }

    private Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth;
    }

    public RoleEnum getRole() {
        return RoleEnum.valueOf(getAuthentication().getAuthorities().toString().replaceAll("\\[|\\]|ROLE_", ""));
    }

    public String getUserName() {
        Pattern pattern = Pattern.compile("userName=([^,]*)");
        Matcher matcher = pattern.matcher(getAuthentication().getPrincipal().toString());

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return "";
        }
    }
}
