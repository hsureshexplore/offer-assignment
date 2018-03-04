package com.worldpay.exercise.app;

import com.worldpay.exercise.datasource.DBManager;
import com.worldpay.exercise.domain.Offer;
import com.worldpay.exercise.exception.ServiceException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class OfferServiceImpl implements OfferService {

    private final DBManager dbManager;

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
        Optional<Offer> offer = dbManager.getOffer(id);
        offer.orElseThrow(() -> new ServiceException("No offer found for id [" + id + "]", 404));
        return offer.get();
    }

    @Override
    public Offer cancelOffer(String id) {
        Optional<Offer> offer = dbManager.getOffer(id);
        offer.orElseThrow(() -> new ServiceException("Unable to cancel offer. No offer found for id [" + id + "]", 404));
        return dbManager.cancelOffer(id);
    }
}
