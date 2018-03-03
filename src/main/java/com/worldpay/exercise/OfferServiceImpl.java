package com.worldpay.exercise;

import com.worldpay.exercise.datasource.DBManager;
import com.worldpay.exercise.offer.Offer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

public class OfferServiceImpl implements OfferService {

    private final DBManager dbManager;
    private AtomicLong offerIdCounter = new AtomicLong();

    public OfferServiceImpl(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Offer createOffer(final String description, final BigDecimal price, final String currency, final int validityInSeconds) {
        Offer createdOffer = Offer.create(description, price, currency, validityInSeconds);
        dbManager.addOffer(createdOffer);
        return createdOffer;
    }

    @Override
    public Collection<Offer> getOffers() {
        return new ArrayList<>(dbManager.getOffers());
    }

    @Override
    public Offer getOffer(String id) {
        return dbManager.getOffer(id);
    }

    @Override
    public Offer cancelOffer(String id) {
        return dbManager.cancelOffer(id);
    }
}
