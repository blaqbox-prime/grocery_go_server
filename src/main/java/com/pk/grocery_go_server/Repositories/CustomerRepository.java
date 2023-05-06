package com.pk.grocery_go_server.Repositories;

import com.pk.grocery_go_server.Models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer,String> {
    Customer findByEmail(String email);
//    Customer findByEmail(String email);
}
