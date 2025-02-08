package com.lms.api;


import com.lms.dto.SubscriptionDto;
import com.lms.model.stripe.Subscription;
import com.lms.service.SubscriptionService;
import com.lms.util.ApiPaths;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(ApiPaths.SubscriptionCtrl.CTRL)
@CrossOrigin
public class SubscriptionRestController {


    private final SubscriptionService subscriptionService;
    private static Gson gson = new Gson();

    public SubscriptionRestController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }


    @GetMapping
    public ResponseEntity<SubscriptionDto[]> getSub()
    {
        return ResponseEntity.ok(this.subscriptionService.findAll());

    }

    @GetMapping("/{username}")
    public ResponseEntity<Subscription> findByName(@PathVariable(required = true) String username)
    {
        return ResponseEntity.ok(this.subscriptionService.findByName(username));
    }


    @PostMapping()
    /**
     * Used to create a subscription with strpe checkout page
     * @param checkout
     * @return the subscription id
     * @throws StripeException
     */
    public String subscriptionWithCheckoutPage(@RequestBody Subscription subscription) throws StripeException {
        init();
        SessionCreateParams params = new SessionCreateParams.Builder().setSuccessUrl(subscription.getSuccessUrl())
                .setCancelUrl(subscription.getCancelUrl()).addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION).addLineItem(new SessionCreateParams.LineItem.Builder()
                        .setQuantity(1L).setPrice(subscription.getPriceId()).build())
                .build();

        try {
            this.subscriptionService.save(subscription);
            Session session = Session.create(params);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("sessionId", session.getId());
            return gson.toJson(responseData);
        } catch (Exception e) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", e.getMessage());
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("error", messageData);
            return gson.toJson(responseData);
        }
    }

    private static void init() {
        Stripe.apiKey = "sk_test_51LG389KGVegvSJ1zI5pHikkOEBvJM0xszhl6yraSvYRXoTIJIcBCZeV1LEloZDj09681DnvkoTmCYTzQ6aFOnW5500k1OfDJsO";
    }
}
