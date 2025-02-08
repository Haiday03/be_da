package com.lms.service.imp;

import com.lms.dto.SubscriptionDto;
import com.lms.model.stripe.Subscription;
import com.lms.repository.SubscriptionRepository;
import com.lms.service.SubscriptionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class SubscriptionServiceImp implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final ModelMapper modelMapper;

    public SubscriptionServiceImp(SubscriptionRepository subscriptionRepository, ModelMapper modelMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Subscription save(Subscription subscription) {
        return this.subscriptionRepository.save(subscription);
    }

    public SubscriptionDto[] findAll() {

        SubscriptionDto[] subscriptionDtos = modelMapper.map(this.subscriptionRepository.findAll(), SubscriptionDto[].class);
        return subscriptionDtos;

    }

    public Subscription findByName(String username)
    {
        return this.subscriptionRepository.findByName(username);
    }
}
