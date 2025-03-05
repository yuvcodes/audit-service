package com.microservices.auditservice.repository;

import com.microservices.auditservice.entity.auth.Role;
import com.microservices.auditservice.model.enums.RoleEnum;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, ObjectId> {
    Optional<Role> findByName(RoleEnum name);
}
