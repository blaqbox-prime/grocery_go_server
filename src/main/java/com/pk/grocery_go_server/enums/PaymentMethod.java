package com.pk.grocery_go_server.enums;

public enum PaymentMethod {
    CASH("Cash"),
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    PAYPAL("PayPal"),
    APPLE_PAY("Apple Pay"),
    GOOGLE_PAY("Google Pay");

    private String value;

    private PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
