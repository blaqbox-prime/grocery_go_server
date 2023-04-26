package com.pk.grocery_go_server.Models;

public class CartItem {
    private Product product;
    private int quantity = 1;

    public void incQty(){quantity++;}
    public void decQty(){
        if(this.quantity > 0 ){
            quantity--;
        }
    }

    public CartItem() {
    }

    public CartItem(Product product){
        this.product = product;
        this.quantity = 1;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice(){
        return product.getPrice() * quantity;
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
