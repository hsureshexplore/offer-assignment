package com.worldpay.exercise;

import com.worldpay.exercise.datasource.DBManager;
import com.worldpay.exercise.datasource.DBManagerImpl;
import com.worldpay.exercise.response.JsonUtil;
import com.worldpay.exercise.scheduler.OfferScheduler;

import static spark.Spark.*;

public class OfferServer {

    private final OfferController offerController;

    public OfferServer(final OfferController offerController) {
        this.offerController = offerController;
    }

    public static void main(String[] args) {
        DBManager dbManager = new DBManagerImpl();
        OfferServiceImpl offerService = new OfferServiceImpl(dbManager);

        //Instantiate server and init routes
        new OfferServer(new OfferController(offerService)).initRoutes();

        //Scheduler instantiated
        new OfferScheduler(offerService);
    }

    public void initRoutes() {
        before((req, res) -> {
            res.type("application/json");
        });

        get("/ping", (req, res) -> "OK");
        post("/offers", (req, res) -> offerController.createOffer(req), JsonUtil::toJson);
        get("/offers", (req, res) -> offerController.getOffers(req), JsonUtil::toJson);
        get("/offers/:id", (req, res) -> offerController.getOffer(req), JsonUtil::toJson);
        post("/offers/cancel/:id", (req, res) -> offerController.cancelOffer(req), JsonUtil::toJson);

    }
}
