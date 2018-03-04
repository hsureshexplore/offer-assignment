package com.worldpay.exercise.datasource;

import com.google.common.annotations.VisibleForTesting;
import com.worldpay.exercise.domain.Offer;
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
    public static final String OFFER_DB = "./offer.db";
    private Map<String, Offer> map;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBManagerImpl.class);


    public DBManagerImpl() {
        this(OFFER_DB);
    }
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
        Offer offer = map.get(id);
        LOGGER.debug("Cancelling offer in DBManager {}", offer.getId());
        offer.cancel();
        map.put(offer.getId(), offer);
        LOGGER.info("Cancelled offer added to DB {}", offer.getId());
        return offer;
    }
}
