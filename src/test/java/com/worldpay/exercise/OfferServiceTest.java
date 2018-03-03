package com.worldpay.exercise;

import com.worldpay.exercise.datasource.DBManager;
import com.worldpay.exercise.offer.Offer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;


public class OfferServiceTest {
    private OfferService offerService;
    @Mock
    private DBManager dbManager;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        offerService = new OfferServiceImpl(dbManager);
    }

    @Test
    public void testCreateOfferReturnsIdOnSuccess(){
        //setup
        String description = "Black friday sale. 2 cup cakes for the price of 1. Grab them before they are gone";
        BigDecimal price = new BigDecimal(2.0);
        String currency = "GBP";
        int validityInSeconds = 10;

        //act
        Offer offer = offerService.createOffer(description, price, currency, validityInSeconds);

        //assert
        assertNotNull(offer);
        verify(dbManager).addOffer(offer);
        assertNotNull(offer.getId());
    }
}
