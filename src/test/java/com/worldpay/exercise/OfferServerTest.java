package com.worldpay.exercise;

import com.worldpay.exercise.datasource.DBManagerImpl;
import com.worldpay.exercise.offer.Offer;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OfferServerTest {
    private static String url;
    private static String path;
    private static Offer offer1, offer2, offer3, offer4;

    @BeforeClass
    public static void setUp() {
        //Create Offer server and init routes
        OfferServer offerServer = new OfferServer(new OfferController(new OfferServiceImpl(new DBManagerImpl())));
        offerServer.initRoutes();
        url = "http://localhost:4567";
        path = "/offers";

        //Waiting for server startup
        Spark.awaitInitialization();


        //precreate offers
        offer1 = addOffer("offer1", new BigDecimal(1.5), "GBP", 1);
        offer2 = addOffer("offer2", new BigDecimal(2.5), "GBP", 2);
        offer3 = addOffer("offer3", new BigDecimal(3.5), "GBP", 3);
        offer4 = addOffer("offer4", new BigDecimal(4.5), "GBP", 4);
    }

    @AfterClass
    public static void destroy() {
        Spark.stop();
    }

    @Test
    public void testPingReturnsOK() {
        String path = "/ping";
        given().
                when().get(url + path).
                then().statusCode(is(200)).
                body(is("OK"));
    }

    @Test
    public void unavailableRouteReturnsNotFound() {
        String path = "/unavailable";
        given().
                when().get(url + path).
                then().statusCode(is(404));
    }

    @Test
    public void testAddOfferReturnsOfferResponse() {
        String description = "Black friday sale. 2 cup cakes for the price of 1. Grab them before they are gone";
        BigDecimal price = BigDecimal.TEN;
        String currency = "GBP";
        int validityInSeconds = 10;

        Offer offer = addOffer(description, price, currency, validityInSeconds);
        assertNotNull(offer.getId());
        assertEquals(offer.getDescription(), description);
        assertEquals(offer.getPrice(), price);
        assertEquals(offer.getCurrency(), currency);
        assertEquals(offer.getValidityInSeconds(), validityInSeconds);
        assertNotNull(offer.getCreatedAt());
        assertTrue(offer.isValid());
    }

    @Test
    public void testGetOffersReturnsListOfOffers() {
        Response response = given().
                when().
                get(url + path).
                then().
                statusCode(is(200)).
                extract().response();
        List<Offer> offerList = Arrays.asList(response.getBody().as(Offer[].class));
        assertThat(offerList, Matchers.hasItems(offer1, offer2, offer3, offer4));
    }

    @Test
    public void testGetOfferByIdReturnsCorrespondingOffer() {
        Offer offer = addOffer("offer5", new BigDecimal(5.5), "USD", 5);
        Response response = given().
                when().
                get(url + path + "/" + offer.getId()).
                then().
                statusCode(is(200)).
                extract().response();
        Offer returnedOfferById = response.getBody().as(Offer.class);
        assertEquals(offer, returnedOfferById);
    }

    @Test
    public void testCancelOfferReturnsOfferWithValiditySetToFalse() {
        Offer offer = addOffer("offer5", new BigDecimal(5.5), "USD", 5);
        Response response = given().
                when().
                post(url + path + "/cancel/" + offer.getId()).
                then().
                statusCode(is(200)).
                extract().response();
        Offer returnedOfferById = response.getBody().as(Offer.class);
        assertFalse(returnedOfferById.isValid());
    }

    private static Offer addOffer(final String description, final BigDecimal price, final String currency, final int validityInSeconds) {
        Response response = given().queryParam("description", description).
                queryParam("price", price).
                queryParam("currency", currency).
                queryParam("validityInSeconds", validityInSeconds).
                post(url + "/offers").
                then().
                statusCode(is(200)).
                extract().
                response();
        return response.getBody().as(Offer.class);
    }
}
