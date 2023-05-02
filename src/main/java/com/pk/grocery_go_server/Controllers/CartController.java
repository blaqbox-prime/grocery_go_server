package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Cart;
import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Order;
import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Services.CartService;
import com.pk.grocery_go_server.Services.CustomerService;
import com.pk.grocery_go_server.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    CustomerService customerService;

    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(@RequestBody Map<String,Object> body){
        String customerId = (String) body.get("customer_id");
        String productId = (String) body.get("product_id");
        double quantity = (Double) body.get("quantity");

        int qty = (int) quantity;

        // Check if the customer exists in the database
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            return new ResponseEntity<>("Customer not found", HttpStatus.BAD_REQUEST);
        }

        // Check if the product exists in the database
        Product product = productService.getProductById(productId);
        if (product == null) {
            return new ResponseEntity<>("Product not found", HttpStatus.BAD_REQUEST);
        }

        cartService.addToCart(customer,product,qty);
        return  new ResponseEntity<>("Product added to cart successfully", HttpStatus.OK);
    }

    @PostMapping("/checkout/{id}")
    public ResponseEntity<String> checkoutCart(@RequestBody Order order ,@PathVariable String id){

        // Check if the customer exists in the database
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }
        if(customer.getCart() == null || customer.getCart().getCartItems().isEmpty()){
            return new ResponseEntity<>("Customer cart is empty", HttpStatus.NOT_FOUND);
        }

        cartService.checkoutCart(customer,order);

        return  new ResponseEntity<>("Cart checked out successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable String id){

        // Check if the customer exists in the database
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return new ResponseEntity<Cart>(new Cart(), HttpStatus.NOT_FOUND);
        }
        if(customer.getCart() == null || customer.getCart().getCartItems().isEmpty()){
            return new ResponseEntity<Cart>(new Cart(), HttpStatus.OK);
        }

        return  new ResponseEntity<>(customer.getCart(), HttpStatus.OK);
    }

    @PostMapping("/remove-from-cart")
    public ResponseEntity<String> RemoveFromCart(@RequestBody Map<String,Object> body){
        String customerId = (String) body.get("customer_id");
        String productId = (String) body.get("product_id");

        // Check if the customer exists in the database
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            return new ResponseEntity<>("Customer not found", HttpStatus.BAD_REQUEST);
        }

        // Check if the product exists in the database
        Product product = productService.getProductById(productId);
        if (product == null) {
            return new ResponseEntity<>("Product not found", HttpStatus.BAD_REQUEST);
        }

        cartService.RemoveFromCart(customer,product);
        return  new ResponseEntity<>("Product removed from cart successfully", HttpStatus.OK);
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestBody Map<String,Object> body){
        String customerId = (String) body.get("customer_id");

        // Check if the customer exists in the database
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            return new ResponseEntity<>("Customer not found", HttpStatus.BAD_REQUEST);
        }

        cartService.clearCart(customer);
        return  new ResponseEntity<>("cart cleared successfully", HttpStatus.OK);
    }

}
