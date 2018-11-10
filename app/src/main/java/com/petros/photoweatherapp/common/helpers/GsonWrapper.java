package com.petros.photoweatherapp.common.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by peter on 09/11/18.
 */

public class GsonWrapper {

    private static GsonWrapper sharedInstance;

    private Gson gson;

    private GsonWrapper() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    public synchronized static GsonWrapper getInstance() {
        if (sharedInstance == null) {
            sharedInstance = new GsonWrapper();
        }
        return sharedInstance;
    }

    public Gson getGson() {
        return gson;
    }
}
