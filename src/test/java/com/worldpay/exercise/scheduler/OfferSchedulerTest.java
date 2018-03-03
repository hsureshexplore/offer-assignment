package com.worldpay.exercise.scheduler;

import com.worldpay.exercise.OfferService;
import com.worldpay.exercise.OfferServiceImpl;
import com.worldpay.exercise.datasource.DBManager;
import com.worldpay.exercise.datasource.DBManagerImpl;
import com.worldpay.exercise.offer.Offer;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class OfferSchedulerTest {

    private DBManager dbManager;

    @Before
    public void before() {

        dbManager = new DBManagerImpl(new ConcurrentHashMap<>());
        OfferService offerService = new OfferServiceImpl(dbManager);
        new OfferScheduler(offerService);
    }

    @Test
    public void testOfferExpiry() throws InterruptedException {
        //setup
        Offer offer1 = Offer.create("Offer description 1", BigDecimal.TEN, "GBP", 1);
        Offer offer2 = Offer.create("Offer description 2", BigDecimal.TEN, "GBP", Integer.MAX_VALUE);
        dbManager.addOffer(offer1);
        dbManager.addOffer(offer2);

        //act
        Thread.sleep(3000);

        //assert
        assertFalse(offer1.isValid());
        assertTrue(offer2.isValid());
    }
}
