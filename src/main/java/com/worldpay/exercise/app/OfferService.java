package com.worldpay.exercise.app;

import com.worldpay.exercise.domain.Offer;

import java.math.BigDecimal;
import java.util.Collection;


public interface OfferService {
    Offer createOffer(final String description, final BigDecimal price, final String currency, int validityInSeconds);

    Collection<Offer> getOffers();

    Offer getOffer(String id);

    Offer cancelOffer(String id);
}

