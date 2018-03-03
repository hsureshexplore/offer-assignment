package com.worldpay.exercise.datasource;

import com.worldpay.exercise.domain.Offer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/***
 * This is test to ensure offer can be added, updated and fetched correctly from DB.
 * It is also useful to verify Custom OfferSerializer works fine
 */
public class DBManagerTest {
    private static DBManager dbManager;

    @BeforeClass
    public static void setUp(){
        dbManager = new DBManagerImpl("./" + DBManagerTest.class.getSimpleName() + "offer.db");
    }

    @Test
    public void testOfferCanBeAddedAndFetchedCorrectly() {
        Offer offer = Offer.create("offer description", new BigDecimal(10.45), "GBP", 2000);
        dbManager.addOffer(offer);
        Offer fetchedOffer = dbManager.getOffer(offer.getId());
        assertEquals(offer, fetchedOffer);
    }

    @Test
    public void testGetOffersReturnsListOffAllOffers(){
        Offer offer1 = Offer.create("offer1", new BigDecimal(10.45), "GBP", 2000);
        Offer offer2 = Offer.create("offer2", new BigDecimal(9), "USD", 1000);
        dbManager.addOffer(offer1);
        dbManager.addOffer(offer2);
        assertTrue(dbManager.getOffers().contains(offer1));
        assertTrue(dbManager.getOffers().contains(offer2));
    }

    @Test
    public void testCancelOfferSetsOfferValidityToFalse(){
        Offer offer3 = Offer.create("offer3", new BigDecimal(10.45), "GBP", 2000);
        dbManager.addOffer(offer3);
        dbManager.cancelOffer(offer3.getId());
        assertFalse(dbManager.getOffer(offer3.getId()).isValid());
    }
}
