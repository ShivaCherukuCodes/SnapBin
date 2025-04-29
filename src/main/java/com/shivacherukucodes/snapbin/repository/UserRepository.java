package com.shivacherukucodes.snapbin.repository;

import com.shivacherukucodes.snapbin.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
