package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.User;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Services.AuthService;
import com.pk.grocery_go_server.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    CustomerRepository repo;

    @Autowired
    CustomerService customerService;

    @Autowired
    AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUpUser(@RequestBody User body){
        try {
           Object user = authService.createUser(body.getEmail(), body.getPassword(),body.getRole());
           return new ResponseEntity<>(user,HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Object> signInUser(@RequestBody Map<String,String> body){
        try {
            User user = (User) authService.authenticate(body.get("email"), body.get("password"));
            return new ResponseEntity<>(user,HttpStatus.OK);
        } catch (Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("message",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
    }

    @PostMapping("/create-customer")
    public Customer createCustomer(@RequestBody Customer body){
//        Customer customer = new Customer();
        return repo.save(body);
    }

    @PostMapping("/update-customer/{id}")
    public Customer updateCustomer(@PathVariable String id, @RequestBody Customer newDetails){
        return customerService.updateDetails(id, newDetails);
    }


}
