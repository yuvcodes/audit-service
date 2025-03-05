package com.microservices.auditservice.model.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUser {
    private String userName;

    private String password;

}
