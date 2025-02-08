package com.lms.repository;

import com.lms.model.stripe.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    public Subscription save(Subscription subscription);

    public Subscription findByName(String username);



}
