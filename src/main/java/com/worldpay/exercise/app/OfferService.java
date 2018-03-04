package com.worldpay.exercise.app;

import com.worldpay.exercise.domain.Offer;

import java.util.Collection;


public interface OfferService {
    void createOffer(Offer offer);

    Collection<Offer> getOffers();

    Offer getOffer(String id);

    Offer cancelOffer(String id);
}

