package com.pk.grocery_go_server.Services;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.sk}")
    private String apiKey;

    @Autowired
    CustomerService customerService;

    public PaymentIntent createPaymentIntent(Order order) throws StripeException {

        Stripe.apiKey = apiKey;

        Customer customer = customerService.getCustomerById(order.getCustomer_id());

        List<String> paymentTypes = new ArrayList<>();
        paymentTypes.add("apple pay");
        paymentTypes.add("card");
        paymentTypes.add("google pay");

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount((long) order.getTotal())
                        .setCurrency("zar")
                        .addAllPaymentMethodType(paymentTypes)
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent;
    }

    public StripeService() throws StripeException {
    }
}
