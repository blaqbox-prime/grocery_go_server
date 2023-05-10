package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Order;
import com.pk.grocery_go_server.Repositories.OrderRepository;
import com.pk.grocery_go_server.Services.CustomerService;
import com.pk.grocery_go_server.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderService orderService;

    @Autowired
    MongoTemplate mongoTemplate;


    @GetMapping("/")
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    @GetMapping(path = "/", params = "status")
    public List<Order> getOrdersByStatus(@RequestParam String status){
        Query query = new Query(Criteria.where("deliveryStatus").is(status));
        return mongoTemplate.find(query, Order.class);
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

//    GET ALL ORDERS THAT ARE NEW
@GetMapping("/preparing")
public ResponseEntity<Object> getAllOrdersWithDeliveryStatusPreparing() {
    try {
        List<Order> orders = orderRepository.findByDeliveryStatus("Preparing");
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }catch(Exception e){
        Map<String, Object> map = new HashMap<>();
        map.put("message",e.getMessage());
        return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @GetMapping("/completed")
    public List<Order> getAllOrdersWithDeliveryStatusCompleted() {
        return orderRepository.findByDeliveryStatus("Completed");
    }

//    GET ORDERS BY CUSTOMER
    @GetMapping("/orders/customer/{customerId}")
    public List<Order> getOrdersByCustomer(@PathVariable String customerId) {
        return orderService.getAllCustomerOrders(customerId);
    }

//    UPDATE ORDER
    @PostMapping("/{id}")
    public ResponseEntity<Object> updateOrder(@PathVariable String id, @RequestBody Order updatedOrder){
        Order order = orderService.getOrderById(id);
        System.out.println("-----------------------------------\nOrder From DB\n-----------------------------------");
        System.out.println(order.toString());
        System.out.println("-----------------------------------\nOrder From App\n-----------------------------------");
        System.out.println(updatedOrder.toString());

        Map<String,Object> map = new HashMap<>();
        if (order != null) {
            order.setPaymentMethod(updatedOrder.getPaymentMethod());
            order.setDeliveryStatus(updatedOrder.getDeliveryStatus());
            order.setAddress(updatedOrder.getAddress());

            order.setDeliveryFee(updatedOrder.getDeliveryFee());
            order.setItems(updatedOrder.getItems());
            order.setDeliveryMethod(updatedOrder.getDeliveryMethod());

           Order savedOrder = orderRepository.save(order);
            return new ResponseEntity<>(savedOrder,HttpStatus.OK);
        }else {
            map.put("message","Failed to update order");
            return new ResponseEntity<>(map,HttpStatus.NOT_MODIFIED);
        }


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
