package com.pk.grocery_go_server.Repositories;

import com.pk.grocery_go_server.Models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order,String> {
}
