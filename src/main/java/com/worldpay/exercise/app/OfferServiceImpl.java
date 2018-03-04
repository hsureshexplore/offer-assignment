package com.worldpay.exercise.app;

import com.worldpay.exercise.datasource.DBManager;
import com.worldpay.exercise.domain.Offer;
import com.worldpay.exercise.exception.ServiceException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class OfferServiceImpl implements OfferService {

    private final DBManager dbManager;

    public OfferServiceImpl(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void createOffer(Offer offer) {
        dbManager.addOffer(offer);
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
        return dbManager.cancelOffer(id);
    }
}
