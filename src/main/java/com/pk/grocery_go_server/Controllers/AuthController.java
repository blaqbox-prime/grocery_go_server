package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    CustomerRepository repo;

//    Create Customer
//    public ResponseEntity createCustomer(@RequestBody Map<String,Object> body){
//        Customer customer = new Customer();
//        return new ResponseEntity<>(customer, HttpStatus.CREATED);
//    }

    @PostMapping("/create-customer")
    public Customer createCustomer(@RequestBody Customer body){
//        Customer customer = new Customer();
        return repo.save(body);
    }


}
