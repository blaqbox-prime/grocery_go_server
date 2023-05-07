package com.pk.grocery_go_server.Models;

import com.pk.grocery_go_server.enums.PaymentMethod;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private Date date;
    private PaymentMethod paymentMethod;

    private String deliveryStatus = "Preparing";
    private Address address;
    private String customer_id;

    private String deliveryMethod;

    private LocalTime TimeSlot;

    double deliveryFee = 0.0;
    private List<OrderItem> items = new ArrayList<>();
    private double total = calculateTotal();

    public Order(Date date, PaymentMethod paymentMethod, Address address, String customer_id) {
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.customer_id = customer_id;
    }

    public double calculateTotal() {
        double total = items.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum() + deliveryFee;
        this.total = total;
        return total;
    }
}
