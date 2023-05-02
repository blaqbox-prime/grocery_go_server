package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Order;
import com.pk.grocery_go_server.Repositories.OrderRepository;
import com.pk.grocery_go_server.Services.CustomerService;
import com.pk.grocery_go_server.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderService orderService;

    @GetMapping("/")
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    @GetMapping("/{customerId}/all")
    public List<Order> getAllCustomerOrders(@PathVariable String customerId){
        return orderService.getAllCustomerOrders(customerId);
    }

    @PostMapping("/{customerId}/completed")
    public ResponseEntity<List<Order>> getAllCustomerCompletedOrders(@PathVariable String customerId){
        // Check if the customer exists in the database
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            return new ResponseEntity<>(new ArrayList<>() , HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(orderService.getAllCustomerCompletedOrders(customerId), HttpStatus.OK);
        }
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId){
        Order order = orderService.getOrderById(orderId);

        if(order != null){
            boolean cancelled = orderService.cancelOrder(orderId);
            if (cancelled){
                return new ResponseEntity<>("Order cancelled Successfully",HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Failed to cancel order", HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/{orderId}/update-status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String,Object> body){
        Order order = orderService.getOrderById(orderId);
        if(order != null){
            String newStatus = (String) body.get("newStatus");
            orderService.updateOrderStatus(newStatus,orderId);
                return new ResponseEntity<>("Order Updated Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Order does not exist", HttpStatus.NOT_MODIFIED);
    }

}
