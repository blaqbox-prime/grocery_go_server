package com.pk.grocery_go_server.Controllers;

import com.google.gson.Gson;
import com.pk.grocery_go_server.Models.Order;
import com.pk.grocery_go_server.Repositories.OrderRepository;
import com.pk.grocery_go_server.Services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    StripeService paymentService;

    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String,String>> createPayment(@RequestBody Map<String,Object> body) {
        String order_id = (String) body.get("order_id");

        Optional<Order> order = orderRepository.findById(order_id);
        Map<String, String> map = new HashMap<>();

        if(!order.isPresent()){
            map.put("message", "Order does not exist");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else {
            Gson gson = new Gson();

           try{
               PaymentIntent intent = paymentService.createPaymentIntent(order.get());
               map.put("client_secret", intent.getClientSecret());
               return new ResponseEntity<>(map,HttpStatus.CREATED);
           }catch (StripeException e){
               map.put("error", e.getMessage());
               return new ResponseEntity<>(map,HttpStatus.CREATED);
           }
        }
    }


}
