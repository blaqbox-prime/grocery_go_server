package com.pk.grocery_go_server.Models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingList {
    private String title;
    private List<Product> items = new ArrayList<>();

    public boolean addItem(Product product){
        return items.add(product);
    }
    public boolean removeItem(Product product){
        return items.remove(product);
    }
}
