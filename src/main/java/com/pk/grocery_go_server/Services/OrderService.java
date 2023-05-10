package com.pk.grocery_go_server.Services;

import com.mongodb.client.MongoClient;
import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Order;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoConverter converter;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    CustomerRepository customerRepository;
    public List<Order> getAllCustomerOrders(String customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        Query query = new Query(Criteria.where("customer").is(customer));
        return mongoTemplate.find(query, Order.class);
    }

    public Order getOrderById(String id){
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    public List<Order> getAllCustomerCompletedOrders(String customerId) {
        Query query = new Query(Criteria.where("customer_id").is(customerId).and("deliveryStatus").is("Completed"));
        return mongoTemplate.find(query, Order.class);
    }

    public boolean cancelOrder(String orderId){
        Order order = orderRepository.findById(orderId).get();
        if(!order.getDeliveryStatus().equals("Shipping") && !order.getDeliveryStatus().equals("Completed")){
            order.setDeliveryStatus("Cancelled");
            orderRepository.save(order);
            return true;
        } else {
            return false;
        }
    }

    public Order updateOrderStatus(String newStatus, String orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()){
            order.get().setDeliveryStatus(newStatus);
            orderRepository.save(order.get());
            return order.get();
        } else {
            return null;
        }
    }

}
