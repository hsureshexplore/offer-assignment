package com.worldpay.exercise.scheduler;

import com.worldpay.exercise.app.OfferService;
import com.worldpay.exercise.domain.Offer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class OfferScheduler {
    public static final int PERIOD = 1;
    public static final int INITIAL_DELAY = 0;
    private OfferService offerService;
    private Logger LOGGER = LoggerFactory.getLogger(OfferScheduler.class);
    private final Clock clock;

    public OfferScheduler(OfferService offerService, Clock clock){
        this.clock = clock;
        this.offerService = offerService;
        clock.register(this::expireOffers);
    }

    private void expireOffers() {
        Collection<Offer> offers = offerService.getOffers();
        LOGGER.debug("Queried offers {}", offers.size());
        offers.stream().filter(Offer::isValid).filter(Offer::shouldExpire).forEach(offer -> {
            LOGGER.info("Cancelling offer {} created at {} with validity period in seconds {}", offer.getId(), offer.getCreatedAt(), offer.getValidityInSeconds());
            offerService.cancelOffer(offer.getId());
        });
    }

    public void start() {
        clock.start();
    }

    public void stop() {
        clock.stop();
    }
}
