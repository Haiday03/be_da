package com.lms.service;
import com.lms.model.stripe.CheckoutPayment;
import com.lms.model.stripe.DashboardInfo;


import java.util.List;

public interface StripeService {

    public CheckoutPayment save(CheckoutPayment checkoutPayment);
    public List<CheckoutPayment> findAll();

    public DashboardInfo getDashboardInfo();


}
