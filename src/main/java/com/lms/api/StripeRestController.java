package com.lms.api;

import java.util.HashMap;
import java.util.Map;
import com.lms.model.stripe.CheckoutPayment;
import com.lms.model.stripe.DashboardInfo;
import com.lms.service.StripeService;
import com.lms.util.ApiPaths;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@RestController
@RequestMapping(ApiPaths.StripeCtrl.CTRL)
@CrossOrigin
public class StripeRestController {

    private final StripeService stripeService;


    // create a Gson object
    private static Gson gson = new Gson();

    public StripeRestController(StripeService stripeService) {
        this.stripeService = stripeService;
    }


    @PostMapping("/payment")
    /**
     * Payment with Stripe checkout page
     *
     * @throws StripeException
     */
    public String paymentWithCheckoutPage(@RequestBody CheckoutPayment payment) throws StripeException {
        // We initilize stripe object with the api key
        init();
        // We create a  stripe session parameters
        SessionCreateParams params = SessionCreateParams.builder()
                // We will use the credit card payment method
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(payment.getSuccessUrl())
                .setCancelUrl(
                        payment.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setQuantity(payment.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(payment.getCurrency()).setUnitAmount(payment.getAmount())
                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                                        .builder().setName(payment.getName()).build())
                                                .build())
                                .build())
                .build();
        // create a stripe session
        Session session = Session.create(params);
        Map<String, String> responseData = new HashMap<>();
        // We get the sessionId and we putted inside the response data you can get more info from the session object
        responseData.put("id", session.getId());
        // We can return only the sessionId as a String


        //insert to database


        this.stripeService.save(payment);
        return gson.toJson(responseData);
    }



    @GetMapping("/orders")
    public ResponseEntity<DashboardInfo> getOrders() throws StripeException {
        return ResponseEntity.ok(this.stripeService.getDashboardInfo());
    }
    @GetMapping
    public ResponseEntity<String> getAllInvoice() throws StripeException {
        init();
        Map<String, Object> params = new HashMap<>();
        InvoiceCollection invoices = Invoice.list(params);

        return ResponseEntity.ok(invoices.toJson());
    }
    @GetMapping("/charges")
    public ResponseEntity<String> getAllCharge() throws StripeException {
        init();
        Map<String, Object> params = new HashMap<>();
        ChargeCollection charges = Charge.list(params);
        return ResponseEntity.ok(charges.toJson());
    }
    private static void init() {
        Stripe.apiKey = "sk_test_51LG389KGVegvSJ1zI5pHikkOEBvJM0xszhl6yraSvYRXoTIJIcBCZeV1LEloZDj09681DnvkoTmCYTzQ6aFOnW5500k1OfDJsO";
    }
}