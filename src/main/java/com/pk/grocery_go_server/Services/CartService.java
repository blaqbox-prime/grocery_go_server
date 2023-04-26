package com.pk.grocery_go_server.Services;

import com.pk.grocery_go_server.Models.CartItem;
import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Product;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private ProductRepository productRepo;

    public Customer getCustomerById(String id){
        Optional<Customer> customer = customerRepo.findById(id);
        return customer.orElse(null);
    }

    public Product getProductById(String id){
        Optional<Product> product = productRepo.findById(id);
        return product.orElse(null);
    }

    public void addToCart(Customer customer, Product product){
        CartItem cartItem = new CartItem(product);
        customer.getCart().addToCart(cartItem);
        customerRepo.save(customer);
    }
}
