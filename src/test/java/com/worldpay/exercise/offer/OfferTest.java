package com.worldpay.exercise.offer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.junit.Assert.*;


public class OfferTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();;

    @Test
    public void testCreateOfferReturnsAnOffer(){
        //setup
        Long id = 1L;
        String description = "Black friday sale. 2 cup cakes for the price of 1. Grab them before they are gone";
        BigDecimal price = new BigDecimal(2.0);
        String currency = "GBP";
        int validityInSeconds = 10;

        //act
        Offer createdOffer = Offer.create(description, price, currency, validityInSeconds);

        //assert
        assertNotNull(createdOffer);
        assertNotNull(createdOffer.getCreatedAt());
        assertEquals(description, createdOffer.getDescription());
        assertEquals(price, createdOffer.getPrice());
        assertEquals(validityInSeconds, createdOffer.getValidityInSeconds());
        assertEquals(currency, createdOffer.getCurrency());
        assertTrue(createdOffer.isValid());
        assertNotNull(createdOffer.getId());
    }

    @Test
    public void testOfferExpiryReturnTrueAfterValidityPeriod() throws InterruptedException {
        //setup
        Offer createdOffer = Offer.create("offer description", BigDecimal.TEN, "GBP", 3);

        //act
        Thread.sleep(3000);

        //assert
        assertTrue(createdOffer.shouldExpire());
    }

    @Test
    public void testOfferExpiryReturnFalseBeforeValidityPeriod() throws InterruptedException {
        //setup
        Offer createdOffer = Offer.create("offer description", BigDecimal.TEN, "GBP", Integer.MAX_VALUE);

        //act
        Thread.sleep(1000);

        //assert
        assertFalse(createdOffer.shouldExpire());
    }


    @Test
    public void testOfferExpiryReturnFalseBeforeValidityPeriod2() throws InterruptedException {
        //setup
        Offer createdOffer = Offer.create("offer description", BigDecimal.TEN, "GBP", 1000000);

        //act
        Thread.sleep(1000);

        //assert
        assertFalse(createdOffer.shouldExpire());
    }

    @Test
    public void testCreateOfferReturnsErrorWhenDescriptionIsNull(){
        //set expectation
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Description is Mandatory");

        //setup
        Long id = 1L;
        String description = null;
        BigDecimal price = new BigDecimal(2.0);
        String currency = "GBP";
        int validityInSeconds = 10;

        //act
        Offer.create(description, price, currency, validityInSeconds);
    }

    @Test
    public void testCreateOfferReturnsErrorWhenPriceIsNull(){
        //set expectation
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Price is Mandatory");

        //setup
        Long id = 1L;
        String description = "Black friday sale. 2 cup cakes for the price of 1. Grab them before they are gone";
        BigDecimal price = BigDecimal.ZERO;
        String currency = "GBP";
        int validityInSeconds = 10;


        //act
        Offer.create(description, price, currency, validityInSeconds);
    }


    @Test
    public void testCreateOfferReturnsErrorWhenPriceIsZero(){
        //set expectation
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Price is Mandatory");

        //setup
        Long id = 1L;
        String description = "Black friday sale. 2 cup cakes for the price of 1. Grab them before they are gone";
        BigDecimal price = BigDecimal.ZERO;
        String currency = "GBP";
        int validityInSeconds = 10;

        //act
        Offer.create(description, price, currency, validityInSeconds);
    }

    @Test
    public void testCreateOfferReturnsErrorWhenCurrencyIsNull(){
        //set expectation
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Currency is Mandatory");

        //setup
        Long id = 1L;
        String description = "Black friday sale. 2 cup cakes for the price of 1. Grab them before they are gone";
        BigDecimal price = BigDecimal.ONE;
        String currency = null;
        int validityInSeconds = 10;

        //act
        Offer.create(description, price, currency, validityInSeconds);
    }

    @Test
    public void testCreateOfferReturnsErrorWhenvalidityInSecondsIsInvalid(){
        //set expectation
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Validity period should be greater than 0");

        //setup
        Long id = 1L;
        String description = "Black friday sale. 2 cup cakes for the price of 1. Grab them before they are gone";
        BigDecimal price = BigDecimal.ONE;
        String currency = "GBP";
        int validityInSeconds = 0;

        //act
        Offer.create(description, price, currency, validityInSeconds);
    }
}
