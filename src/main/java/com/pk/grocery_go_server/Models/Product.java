package com.pk.grocery_go_server.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Product")
public class Product implements Comparable<Product> {

    @Id
    private String _id;
    private String name;
    private String description;
    private double price;
    private String category;
    private int rating = 3;
    public void setPrice(double price) {
        this.price = price;
    }

    private String image;

    private Inventory inventory;

    List<Review> reviews = new ArrayList<>();

    @Override
    public int compareTo(Product o) {
        return this._id.compareTo(o._id);
    }
}
