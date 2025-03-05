package com.microservices.auditservice.model.users;

import lombok.Data;

@Data
public class LoginUser {
    private String userName;

    private String password;
}
