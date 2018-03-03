package com.worldpay.exercise.response;

import com.google.gson.Gson;

/***
 * ResponseTransformer used to convert objects to Json format.
 * This is used while returning response to rest service calls
 */
public class JsonUtil {

    public static final Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}