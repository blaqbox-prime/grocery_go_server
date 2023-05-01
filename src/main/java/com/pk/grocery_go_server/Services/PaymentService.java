package com.pk.grocery_go_server.Services;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

//    @Autowired
    private StripeService stripeService;

//    public boolean chargeCreditCard(String cardNumber, String cardExpMonth, String cardExpYear,
//                                    String cvc, double amount){
//        //            Create a new charge
//        Charge charge = stripeService.createCharge();
//    }
}
