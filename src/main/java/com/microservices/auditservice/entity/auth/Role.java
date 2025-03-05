package com.microservices.auditservice.entity.auth;

import com.microservices.auditservice.model.enums.RoleEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "role")
@Data
@Builder
public class Role {
    @Id
    private ObjectId id;

    @NonNull
    @Indexed(unique = true)
    private RoleEnum name;

    @NonNull
    private String description;

    @NonNull
    private LocalDateTime createdAt;

    @NonNull
    private LocalDateTime updatedAt;

}
