package com.worldpay.exercise;

import com.worldpay.exercise.offer.Offer;
import spark.Request;

import java.math.BigDecimal;
import java.util.Collection;

public class OfferController {
    private OfferService offerService;

    public OfferController(OfferService offerService){
        this.offerService = offerService;
    }

    public Offer createOffer(Request request) {
        String description = request.queryParams("description");
        BigDecimal price = new BigDecimal(request.queryParams("price"));
        String currency = request.queryParams("currency");
        int validityInSeconds = Integer.parseInt(request.queryParams("validityInSeconds"));
        return offerService.createOffer(description, price, currency, validityInSeconds);
    }

    public Collection<Offer> getOffers(Request req) {
        return offerService.getOffers();
    }

    public Offer getOffer(Request req) {
        return offerService.getOffer(req.params(":id"));
    }

    public Offer cancelOffer(Request req) {
        return offerService.cancelOffer(req.params(":id"));
    }
}
