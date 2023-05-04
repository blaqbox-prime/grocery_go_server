package com.pk.grocery_go_server.Repositories;

import com.pk.grocery_go_server.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
    User findByEmail(String email);
}
