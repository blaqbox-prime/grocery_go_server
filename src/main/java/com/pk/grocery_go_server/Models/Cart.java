package com.pk.grocery_go_server.Models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Document
public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();
    private double total = 0;

    public List<CartItem> getCartItems() {
        return cartItems;
    }


    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Cart() {
    }

    public Cart(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        calculateTotal();
    }

    public void calculateTotal() {
        this.total = cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }

    public void addToCart(CartItem item){
    boolean exists = false;

        for (int i = 0; i < this.cartItems.size(); i++) {
            CartItem cartItem = this.cartItems.get(i);
            if(cartItem.getProduct().compareTo(item.getProduct()) == 0){
                exists = true;
                cartItem.incQty();
            }
        }

        if(!exists) cartItems.add(item);

    calculateTotal();

    }
    

}
