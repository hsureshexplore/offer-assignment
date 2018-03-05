package com.worldpay.exercise.scheduler;

import com.worldpay.exercise.app.OfferService;
import com.worldpay.exercise.app.OfferServiceImpl;
import com.worldpay.exercise.datasource.DBManager;
import com.worldpay.exercise.datasource.DBManagerImpl;
import com.worldpay.exercise.domain.Offer;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;


public class OfferSchedulerTest {

    private DBManager dbManager;
    private  ManualClock clock = new ManualClock();
    private OfferScheduler offerScheduler;

    @Before
    public void before() {

        dbManager = new DBManagerImpl(new ConcurrentHashMap<>());
        OfferService offerService = new OfferServiceImpl(dbManager);
        offerScheduler = new OfferScheduler(offerService, clock);
    }

    @Test
    public void testOfferExpiry() throws InterruptedException {
        //setup
        Offer offer1 = mockOffer("offer1", true, true);
        Offer offer2 = mockOffer("offer2", true, false);
        dbManager.addOffer(offer1);
        dbManager.addOffer(offer2);

        //act
        offerScheduler.start();
        clock.elapseTime();

        //assert
        verify(offer1).cancel();
        verify(offer2, never()).cancel();
    }

    private Offer mockOffer(String id, boolean valid, boolean expire) {
        Offer offer = mock(Offer.class);
        when(offer.getId()).thenReturn(id);
        when(offer.isValid()).thenReturn(valid);
        when(offer.shouldExpire()).thenReturn(expire);
        return offer;
    }
}
