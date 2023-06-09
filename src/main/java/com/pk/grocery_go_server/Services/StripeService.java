package com.pk.grocery_go_server.Services;

import com.pk.grocery_go_server.Models.Customer;
import com.pk.grocery_go_server.Models.Order;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PaymentLinkUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.stripe.param.PaymentIntentCreateParams.AutomaticPaymentMethods;


@Getter
@Service
public class StripeService {

    @Value("${stripe.sk}")
    private String apiKey;

    @Value("${stripe.pk}")
    private String publishableKey;


    @Autowired
    CustomerService customerService;

    public PaymentIntent createPaymentIntent(Order order) throws StripeException {

        Stripe.apiKey = apiKey;

        Customer customer = customerService.getCustomerById(order.getCustomer().get_id());

        List<String> paymentTypes = new ArrayList<>();
        paymentTypes.add(SessionCreateParams.PaymentMethodType.CARD.getValue());

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount((long) order.getTotal() * 100)
                        .setCurrency("zar")
                        .addAllPaymentMethodType(paymentTypes)
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent;
    }

    public StripeService() throws StripeException {
    }
}
