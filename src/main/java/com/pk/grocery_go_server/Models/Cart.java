package com.pk.grocery_go_server.Models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();

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
    }

    public void addToCart(CartItem item){
//        check if item exists
        boolean exists = this.cartItems.stream()
                .anyMatch(cartItem ->  cartItem.getProduct().get_id() == item.getProduct().get_id());

//        if not then add
        if(exists){
            int idx;
            for (int i = 0; i < cartItems.size(); i++){
                if(cartItems.get(i).getProduct().get_id().equals(item.getProduct().get_id())){
                    cartItems.get(i).incQty();
                }
            }
        } else {
            cartItems.add(item);
        }

    }

//    Remove from cart

}
