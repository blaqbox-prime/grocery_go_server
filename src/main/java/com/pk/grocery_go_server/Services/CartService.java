package com.pk.grocery_go_server.Services;

import com.pk.grocery_go_server.Models.*;
import com.pk.grocery_go_server.Repositories.CustomerRepository;
import com.pk.grocery_go_server.Repositories.OrderRepository;
import com.pk.grocery_go_server.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderRepository orderRepo;


    public void addToCart(Customer customer, Product product, int qty){

        CartItem cartItem = new CartItem(product);
        if (customer.getCart() == null){
            customer.setCart(new Cart());
        }

        if(qty > 1) {
            cartItem.setQuantity(qty);
            customer.getCart().addToCart(cartItem);
        } else {
            customer.getCart().addToCart(cartItem);
        }
        customerRepo.save(customer);
    }

    public void RemoveFromCart(Customer customer, Product product){

//        For debugging
        customer.toString();
        product.toString();


        customer.getCart().removeItem(product);
        customerRepo.save(customer);
    }


    //    Checkout Cart -> Creates an order from the users cart and clears the cart;
    public Order checkoutCart(Customer customer, Order order){
        System.out.println(customer.toString());
        try{
            Cart cart = customer.getCart();
            order.setCustomer(customer);
            order.setItems(
                    cart.getCartItems()
                            .stream()
                            .map(item -> new OrderItem(item.getProduct(),item.getQuantity()))
                            .collect(Collectors.toList())
            );
//            order.calculateTotal();

            customer.setCart(new Cart());
            customerRepo.save(customer);

           Order savedOrder = orderRepo.save(order);
           return savedOrder;
        } catch(Exception e){
            System.out.println("Checkout Exception: " + e.getMessage());
            return null;
        }
    }

    public void clearCart(Customer customer){
        customer.setCart(new Cart());
        customerRepo.save(customer);
    }
}
