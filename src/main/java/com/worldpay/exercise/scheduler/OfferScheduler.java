package com.worldpay.exercise.scheduler;

import com.worldpay.exercise.OfferService;
import com.worldpay.exercise.offer.Offer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OfferScheduler {
    private OfferService offerService;
    private Logger LOGGER = LoggerFactory.getLogger(OfferScheduler.class);

    public OfferScheduler(OfferService offerService){
        this.offerService = offerService;
        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::expireOffers, 0, 1, TimeUnit.SECONDS);

    }

    private void expireOffers() {
        Collection<Offer> offers = offerService.getOffers();
        LOGGER.debug("Queried offers {}", offers.size());
        offers.stream().filter((offer) -> (offer.isValid() && offer.shouldExpire())).forEach(offer -> {
            LOGGER.info("Cancelling offer {} created at {} with validity period in seconds {}", offer.getId(), offer.getCreatedAt(), offer.getValidityInSeconds());
            offerService.cancelOffer(offer.getId());
        });
    }
}
