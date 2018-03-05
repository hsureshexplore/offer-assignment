package com.worldpay.exercise;

import com.worldpay.exercise.app.OfferServiceImpl;
import com.worldpay.exercise.datasource.DBManager;
import com.worldpay.exercise.datasource.DBManagerImpl;
import com.worldpay.exercise.exception.ServiceException;
import com.worldpay.exercise.response.JsonUtil;
import com.worldpay.exercise.response.ResponseError;
import com.worldpay.exercise.scheduler.Clock;
import com.worldpay.exercise.scheduler.OfferScheduler;
import com.worldpay.exercise.scheduler.RealClock;

import java.util.concurrent.TimeUnit;

import static spark.Spark.*;

/***
 * Entry point for the application.
 */
public class OfferServer {

    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_REQUEST = 400;
    public static final int RESOURCE_NOT_FOUND = 404;
    private final OfferController offerController;
    public static final String OFFER_DB = "./offer.db";

    public OfferServer(final OfferController offerController) {
        this.offerController = offerController;
    }

    public static void main(String[] args) {
        createServer(OFFER_DB);
    }

    /**
     * In real application all the bean wiring and lifecycle operations will be done in a DI framework like Spring,
     * Guice, Play framework
     * @return an instance of offer server
     * @param offerDb
     */
    public static OfferServer createServer(String offerDb) {
        DBManager dbManager = new DBManagerImpl(offerDb);
        OfferServiceImpl offerService = new OfferServiceImpl(dbManager);

        //Instantiate server and init routes
        OfferServer offerServer = new OfferServer(new OfferController(offerService));
        offerServer.initRoutes();

        //Scheduler instantiated
        Clock clock = new RealClock(1, TimeUnit.SECONDS);
        new OfferScheduler(offerService, clock);
        clock.start();
        return offerServer;
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

        exception(Exception.class, (e, req, res) -> {

            int statusCode = INTERNAL_SERVER_ERROR;
            if (e instanceof IllegalArgumentException) {
                statusCode = BAD_REQUEST;
            }
            else if (e instanceof ServiceException) {
                statusCode = RESOURCE_NOT_FOUND;
            }
            res.status(statusCode);
            res.body(JsonUtil.toJson(new ResponseError(e)));
        });
    }
}
