package com.worldpay.exercise.datasource;

import com.worldpay.exercise.offer.Offer;

import java.util.Collection;


public interface DBManager {
    void addOffer(final Offer offer);

    Collection<Offer> getOffers();

    Offer getOffer(String id);

    Offer cancelOffer(String id);
}
