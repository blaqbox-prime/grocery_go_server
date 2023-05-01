package com.pk.grocery_go_server.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Product")
public class Product implements Comparable<Product> {

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Id
    private String _id;
    private String name;
    private String description;
    private double price;
    private String category;
    public void setPrice(double price) {
        this.price = price;
    }

    private String imageUrl;

    private Inventory inventory;

    @Override
    public int compareTo(Product o) {
        return this._id.compareTo(o._id);
    }
}
