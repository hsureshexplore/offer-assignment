package com.worldpay.exercise.datasource;

import com.google.common.annotations.VisibleForTesting;
import com.worldpay.exercise.offer.Offer;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;



//TODO Fix warnings
public class DBManagerImpl implements DBManager {

    public static final String OFFER = "offer";
    private Map<String, Offer> map;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBManagerImpl.class);
    public DBManagerImpl(){
        this(DBMaker.fileDB("./offer.db")
                .transactionEnable()
                .closeOnJvmShutdown()
                .make().<String, Offer>hashMap(OFFER, Serializer.STRING, Serializer.JAVA).createOrOpen());
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
    public Offer getOffer(String id) {
        return map.get(id);
    }

    @Override
    public Offer cancelOffer(String id) {
        Offer offer = getOffer(id);
        LOGGER.info("Cancelling offer in DBManager {}", offer.getId());
        offer.cancel();
        map.put(offer.getId(), offer);
        LOGGER.info("Cancelled offer added to DB {}", offer.getId());
        return offer;
    }
}
