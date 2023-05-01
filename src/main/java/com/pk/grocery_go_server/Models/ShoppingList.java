package com.pk.grocery_go_server.Models;

import lombok.*;

import java.util.List;
import java.util.TreeSet;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingList {
    private String title;
    private TreeSet<Product> items;

    public boolean addItem(Product product){
        return items.add(product);
    }
}
