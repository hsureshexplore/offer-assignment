package com.worldpay.exercise.datasource;

import com.worldpay.exercise.domain.Offer;

import java.util.Collection;
import java.util.Optional;


public interface DBManager {
    void addOffer(final Offer offer);

    Collection<Offer> getOffers();

    Optional<Offer> getOffer(String id);

    Offer cancelOffer(String id);
}
