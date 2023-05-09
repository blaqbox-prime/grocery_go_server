package com.pk.grocery_go_server.Repositories;

import com.pk.grocery_go_server.Models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order,String> {
    List<Order> findByDeliveryStatus(String preparing);

    List<Order> findByCustomer__id(String customerId);
}
