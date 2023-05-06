package com.pk.grocery_go_server.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customer")
public class Customer {
    @Id
    private String _id;
    private String firstName;
    private String LastName;

    @DBRef
    User user;

    @Indexed(unique = true)
    private String email;
    private String password;

    @Indexed(unique = true)
    private String phone;

    private Cart cart;
    private Address address;
    private List<ShoppingList> shoppingLists = new ArrayList<>();
}
