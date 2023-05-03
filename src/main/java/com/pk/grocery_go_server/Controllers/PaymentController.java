package com.pk.grocery_go_server.Controllers;

import com.google.gson.Gson;
import com.pk.grocery_go_server.Models.Order;
import com.pk.grocery_go_server.Repositories.OrderRepository;
import com.pk.grocery_go_server.Services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    StripeService paymentService;

    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String,String>> createPayment(@RequestBody Map<String,Object> body) {
        String order_id = (String) body.get("order_id");

        Optional<Order> order = orderRepository.findById(order_id);
        Map<String, String> map = new HashMap<>();

        if(!order.isPresent()){
            map.put("message", "Order does not exist");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } else {
            Gson gson = new Gson();

           try{
               PaymentIntent intent = paymentService.createPaymentIntent(order.get());
               map.put("client_secret", intent.getClientSecret());
               return new ResponseEntity<>(map,HttpStatus.CREATED);
           }catch (StripeException e){
               map.put("error", e.getMessage());
               return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
           }
        }
    }

    // This example sets up an endpoint using the Spark framework.

    @PostMapping("/payment-sheet")
    public ResponseEntity<Map<String, Object>> getPaymentSheet(@RequestBody Map<String, Object> body) throws StripeException {
        Stripe.apiKey = paymentService.getApiKey();
        String order_id = (String) body.get("order_id");

        Optional<Order> order = orderRepository.findById(order_id);

        // Use an existing Customer ID if this is a returning customer.
        CustomerCreateParams customerParams = CustomerCreateParams.builder().build();
        Customer customer = Customer.create(customerParams);

        EphemeralKeyCreateParams ephemeralKeyParams =
                EphemeralKeyCreateParams.builder()
                        .setStripeVersion("2020-08-27")
                        .setCustomer(customer.getId())
                        .build();

        EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams);

        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        PaymentIntentCreateParams paymentIntentParams =
                PaymentIntentCreateParams.builder()
                        .setAmount((long)(order.get().getTotal() * 100))
                        .setCurrency("zar")
                        .setCustomer(customer.getId())
                        .addAllPaymentMethodType(paymentMethodTypes)
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);

        Map<String, Object> responseData = new HashMap();
        responseData.put("paymentIntent", paymentIntent.getClientSecret());
        responseData.put("ephemeralKey", ephemeralKey.getSecret());
        responseData.put("customer", customer.getId());
        responseData.put("publishableKey", paymentService.getPublishableKey());

        return ResponseEntity.ok(responseData);
    }


}
