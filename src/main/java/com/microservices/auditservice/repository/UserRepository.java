package com.microservices.auditservice.repository;

import com.microservices.auditservice.entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Users, ObjectId> {
    Optional<Users> findByUserName(String userName);
}
