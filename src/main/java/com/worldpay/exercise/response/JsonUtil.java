package com.worldpay.exercise.response;

import com.google.gson.Gson;



public class JsonUtil {

    public static final Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}