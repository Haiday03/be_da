package com.lms.repository;

import com.lms.model.stripe.CheckoutPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StripeRepository extends JpaRepository<CheckoutPayment, Long> {


}
