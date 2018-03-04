package com.worldpay.exercise.datasource;

import com.google.common.annotations.VisibleForTesting;
import com.worldpay.exercise.domain.Offer;
import com.worldpay.exercise.exception.ServiceException;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/***
 * Embedded database engine for storing offers.
 * Uses a custom OfferSerializer
 */
public class DBManagerImpl implements DBManager {

    public static final String OFFER = "offer";
    private Map<String, Offer> map;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBManagerImpl.class);

    @VisibleForTesting
    public DBManagerImpl(String dbName){
        this.map = DBMaker.fileDB(dbName)
                .transactionEnable()
                .closeOnJvmShutdown()
                .fileDeleteAfterClose()
                .make().hashMap(OFFER, Serializer.STRING, new OfferSerializer()).createOrOpen();
    }

    @VisibleForTesting
    public DBManagerImpl(Map<String, Offer> map){
        this.map = map;
    }


    @Override
    public void addOffer(Offer offer) {
        map.put(offer.getId(), offer);
    }

    @Override
    public Collection<Offer> getOffers() {
        return map.values();
    }

    @Override
    public Optional<Offer> getOffer(String id) {
        return map.get(id) != null ? Optional.of(map.get(id)) : Optional.empty();
    }

    @Override
    public Offer cancelOffer(String id) {
        Optional<Offer> offerOpt = getOffer(id);
        Offer offer = offerOpt.orElseThrow(() -> new ServiceException("Unable to cancel offer. No offer found for id [" + id + "]"));

        LOGGER.debug("Cancelling offer in DBManager {}", offer.getId());
        offer.cancel();
        map.put(offer.getId(), offer);
        LOGGER.info("Cancelled offer added to DB {}", offer.getId());
        return offer;
    }
}
