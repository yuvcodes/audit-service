package com.microservices.auditservice.model.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;

    private long expiresIn;
}
