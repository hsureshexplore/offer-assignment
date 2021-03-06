package com.worldpay.exercise.scheduler;

import com.worldpay.exercise.app.OfferService;
import com.worldpay.exercise.domain.Offer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OfferScheduler {
    public static final int PERIOD = 1;
    public static final int INITIAL_DELAY = 0;
    private OfferService offerService;
    private Logger LOGGER = LoggerFactory.getLogger(OfferScheduler.class);

    public OfferScheduler(OfferService offerService){
        this.offerService = offerService;
        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::expireOffers, INITIAL_DELAY, PERIOD, TimeUnit.SECONDS);
    }

    private void expireOffers() {
        Collection<Offer> offers = offerService.getOffers();
        LOGGER.debug("Queried offers {}", offers.size());
        offers.stream().filter(Offer::isValid).filter(Offer::shouldExpire).forEach(offer -> {
            LOGGER.info("Cancelling offer {} created at {} with validity period in seconds {}", offer.getId(), offer.getCreatedAt(), offer.getValidityInSeconds());
            offerService.cancelOffer(offer.getId());
        });
    }
}
