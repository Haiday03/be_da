package com.lms.service;

import com.lms.dto.SubscriptionDto;
import com.lms.model.stripe.Subscription;


public interface SubscriptionService {

    public Subscription save(Subscription subscription);
    public SubscriptionDto[] findAll();

    public Subscription findByName(String username);

}
