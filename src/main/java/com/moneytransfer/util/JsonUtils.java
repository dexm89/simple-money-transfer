package com.moneytransfer.util;

import com.google.gson.Gson;

public class JsonUtils {

    private static final Gson GSON = new Gson();

    private JsonUtils() {
    }

    public static String toJson(final Object o) {
        return GSON.toJson(o);
    }

    public static <T> T fromJson(final String json, final Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
}
