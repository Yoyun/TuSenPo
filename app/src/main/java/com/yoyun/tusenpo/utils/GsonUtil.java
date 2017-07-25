package com.yoyun.tusenpo.utils;

import com.google.gson.Gson;

/**
 * Created by Yoyun on 2017/7/13.
 */

public class GsonUtil {

    private static Gson defaultGson;

    public static Gson getDefaultGson() {
        if (defaultGson == null) {
            defaultGson = new Gson();
        }
        return defaultGson;
    }
}
