package com.pk.grocery_go_server.Controllers;

import com.pk.grocery_go_server.Models.Cart;
import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Order;
import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Services.CartService;
import com.pk.grocery_go_server.Services.CustomerService;
import com.pk.grocery_go_server.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @Autowired
    CustomerRepository customerRepository;

    @PostMapping("/{id}/update")
    public ResponseEntity<Map<String,Object>> updateCart(@PathVariable String id, @RequestBody Cart cart){
        Map<String,Object> responseBody = new HashMap<>();

        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            responseBody.put("error", "Customer not found");
            responseBody.put("data", null);
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }else {
            customer.setCart(cart);
            responseBody.put("message","Cart Successfully Updated");
            responseBody.put("data", cart);
            customerRepository.save(customer);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
    }

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
    public ResponseEntity<Object> checkoutCart(@RequestBody Order order ,@PathVariable String id){
        System.out.println(order.toString());

        Map<String,Object> map = new HashMap<>();

        // Check if the customer exists in the database
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            map.put("message","Customer not found");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        if(customer.getCart() == null || customer.getCart().getCartItems().isEmpty()){
            map.put("message","Customer cart is empty");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }

        Order checkedOutOrder = cartService.checkoutCart(customer,order);


        return  new ResponseEntity<>(checkedOutOrder, HttpStatus.OK);
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
    public ResponseEntity<Object> RemoveFromCart(@RequestBody Map<String,Object> body){
        System.out.println(body.get("customer_id"));
        System.out.println(body.get("product_id"));

        String customerId = (String) body.get("customer_id");
        String productId = (String) body.get("product_id");

        Map<String, Object> map = new HashMap<>();
        // Check if the customer exists in the database
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            map.put("message", "Customer not found");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        // Check if the product exists in the database
        Product product = productService.getProductById(productId);
        if (product == null) {
            map.put("message", "Product not found");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }


        try{
            cartService.RemoveFromCart(customer,product);
            map.put("message","Product removed from cart successfully");
            return  new ResponseEntity<>(map, HttpStatus.OK);
        }catch(Exception e){
            map.put("message",e.getMessage());
            return  new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
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
