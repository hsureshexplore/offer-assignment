package com.worldpay.exercise.app;

import com.worldpay.exercise.datasource.DBManager;
import com.worldpay.exercise.domain.Offer;
import com.worldpay.exercise.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class OfferServiceTest {
    private OfferService offerService;
    @Mock
    private DBManager dbManager;
    private Offer offer;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        offer = Offer.create("offer", BigDecimal.TEN, "USD", 4);
        offerService = new OfferServiceImpl(dbManager);
    }

    @Test
    public void testCreateOfferReturnsIdOnSuccess(){
        //act
        offerService.createOffer(offer);

        //assert
        verify(dbManager).addOffer(offer);
    }

    @Test
    public void testGetOffersCallsDbManagerGetOffers(){
        offerService.getOffers();
        verify(dbManager).getOffers();
    }

    @Test
    public void testGetOfferCallsDbManagerGetOffer(){
        String id = "randomid";
        when(dbManager.getOffer(id)).thenReturn(Optional.of(offer));
        offerService.getOffer(id);
        verify(dbManager).getOffer(id);
    }

    @Test(expected = ServiceException.class)
    public void testGetOfferThrowsExceptionWhenOfferIsNotFound(){
        String id = "randomid";
        when(dbManager.getOffer(id)).thenReturn(Optional.empty());
        offerService.getOffer(id);
    }

    @Test
    public void testCancelOfferCallsDbManagerCancelOffer(){
        String id = "randomid";
        when(dbManager.getOffer(id)).thenReturn(Optional.of(offer));
        when(dbManager.cancelOffer(id)).thenReturn(offer);
        offerService.cancelOffer(id);
        verify(dbManager).cancelOffer(id);
    }

    @Test(expected = ServiceException.class)
    public void testCancelOfferThrowsExceptionWhenOfferIsNotFound(){
        String id = "randomid";
        when(dbManager.getOffer(id)).thenReturn(Optional.empty());
        offerService.cancelOffer(id);
    }
}
