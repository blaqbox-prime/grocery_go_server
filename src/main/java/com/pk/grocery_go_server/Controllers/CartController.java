package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(@RequestBody Map<String,Object> body){
        String customerId = (String) body.get("customer_id");
        String productId = (String) body.get("product_id");

        // Check if the customer exists in the database
        Customer customer = cartService.getCustomerById(customerId);
        if (customer == null) {
            return new ResponseEntity<>("Customer not found", HttpStatus.BAD_REQUEST);
        }

        // Check if the product exists in the database
        Product product = cartService.getProductById(productId);
        if (product == null) {
            return new ResponseEntity<>("Product not found", HttpStatus.BAD_REQUEST);
        }

        cartService.addToCart(customer,product);
        return  new ResponseEntity<>("Product added to cart successfully", HttpStatus.OK);
    }
}
