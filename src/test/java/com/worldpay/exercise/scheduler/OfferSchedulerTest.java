package com.worldpay.exercise.scheduler;

import com.worldpay.exercise.datasource.DBManager;
import com.worldpay.exercise.datasource.DBManagerImpl;
import com.worldpay.exercise.offer.Offer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class OfferSchedulerTest {

    private DBManager dbManager;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        dbManager = new DBManagerImpl(new ConcurrentHashMap<>());
        new OfferScheduler(dbManager);
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
