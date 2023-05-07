package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Models.ShoppingList;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerService customerService;

    @GetMapping("/")
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    @GetMapping("/{email}")
    public Customer getCustomer(@PathVariable String email){
        return customerRepository.findByEmail(email);
    }


    @PostMapping("/{id}/create-shopping-list")
    public ResponseEntity<Object> createShoppingList(@RequestBody ShoppingList body, @PathVariable String id){

        ShoppingList shoppingList = customerService.addShoppingList(id,body);

        Map<String, Object> map = new HashMap<>();

        if (shoppingList == null) {
            map.put("message", "Failed to create Shopping List");
            return new ResponseEntity<>(map, HttpStatus.NOT_MODIFIED);
        }
        else {

            return new ResponseEntity<>(shoppingList, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/{id}/", params = "shopping-list")
    public ResponseEntity<Object> addItemToShoppingList(@RequestBody Product body, @PathVariable String id, @RequestParam("shopping-list") String listName){

        ShoppingList shoppingList = customerService.addItemToShoppingList(id,listName,body);

        Map<String, Object> map = new HashMap<>();

        if (shoppingList == null) {
            map.put("message", "Failed to Add Product To Shopping list" + listName);
            return new ResponseEntity<>(map, HttpStatus.NOT_MODIFIED);
        }
        else {
            return new ResponseEntity<>(shoppingList, HttpStatus.OK);
        }
    }
}
