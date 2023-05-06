package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.User;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Repositories.UserRepository;
import com.pk.grocery_go_server.Services.AuthService;
import com.pk.grocery_go_server.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    CustomerRepository repo;

    @Autowired
    CustomerService customerService;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerRepository customerRepository;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUpUser(@RequestBody User body) {
        try {
            // Create the user
            User newUser = (User) authService.createUser(body.getEmail(), body.getPassword(), body.getRole());

            // Create the corresponding customer object
            Customer newCustomer = new Customer();
            newCustomer.setUser(newUser);
            newCustomer.setEmail(newUser.getEmail());

            // Save the customer object to the database
            Customer savedCustomer = customerRepository.save(newCustomer);

            return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
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

//    @PostMapping("/{userid}/create-customer")
//    public ResponseEntity<Object> createCustomer(@PathVariable String userid, @RequestBody Customer body){
//        try {
//            User user = userRepository.find
//            return new ResponseEntity<>(user,HttpStatus.OK);
//        } catch (Exception e){
//            Map<String,Object> map = new HashMap<>();
//            map.put("error", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
//        }
//        return repo.save(body);
//    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable("id") String customerId, @RequestBody Customer body) {
        try {
            Optional<Customer> customerOptional = customerRepository.findById(customerId);
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                customer.setFirstName(body.getFirstName());
                customer.setLastName(body.getLastName());
                customer.setPhone(body.getPhone());

                customer.setCart(body.getCart());
                customer.setAddress(body.getAddress());
                customer.setShoppingLists(body.getShoppingLists());

                Customer updatedCustomer = customerRepository.save(customer);

                return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            }
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
    }
}
