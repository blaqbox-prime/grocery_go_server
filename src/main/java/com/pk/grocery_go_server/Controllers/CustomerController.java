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

import java.util.List;

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

    @PostMapping("/{id}/create-shopping-list")
    public ResponseEntity<String> createShoppingList(@RequestBody ShoppingList body, @PathVariable String id){

        ShoppingList shoppingList = customerService.addShoppingList(id,body);
        if (shoppingList == null) return new ResponseEntity<>("Failed to Create Shopping list",HttpStatus.NOT_MODIFIED);
        else {
            return new ResponseEntity<String>(shoppingList.toString(), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/{id}/", params = "shopping-list")
    public ResponseEntity<String> addItemToShoppingList(@RequestBody Product body, @PathVariable String id, @RequestParam("shopping-list") String listName){

        ShoppingList shoppingList = customerService.addItemToShoppingList(id,listName,body);

        if (shoppingList == null) return new ResponseEntity<>("Failed to Add Product To Shopping list" + listName,HttpStatus.NOT_MODIFIED);
        else {
            return new ResponseEntity<String>(shoppingList.toString(), HttpStatus.OK);
        }
    }
}
