package com.pk.grocery_go_server.Models;

import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Document(collection = "Product")
public class Inventory {
    private int stockAvailability;

    public Inventory() {
    }

    public Inventory(int stockAvailability) {
        this.stockAvailability = stockAvailability;
    }

    public int getStockAvailability() {
        return stockAvailability;
    }

    public void setStockAvailability(int stockAvailability) {
        this.stockAvailability = stockAvailability;
    }
}
