package com.pk.grocery_go_server.Models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Address {
    private String street;
    private String suburb;
    private String city;
    private String province;
    private String zipCode;
}
