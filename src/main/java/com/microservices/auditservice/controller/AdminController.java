package com.microservices.auditservice.controller;

import com.microservices.auditservice.entity.Users;
import com.microservices.auditservice.model.users.RegisterUser;
import com.microservices.auditservice.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admins")
@RestController
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Users> createAdministrator(@RequestBody RegisterUser registerUserInput) {
        Users createdAdmin = userService.createAdministrator(registerUserInput);

        return ResponseEntity.ok(createdAdmin);
    }
}
