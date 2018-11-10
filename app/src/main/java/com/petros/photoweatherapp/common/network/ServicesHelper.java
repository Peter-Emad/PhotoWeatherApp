package com.petros.photoweatherapp.common.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.petros.photoweatherapp.BuildConfig;
import com.petros.photoweatherapp.common.helpers.Constants;
import com.petros.photoweatherapp.common.helpers.GsonWrapper;
import com.petros.photoweatherapp.common.models.LatLng;
import com.petros.photoweatherapp.common.models.dto.GetCurrentWeatherResponse;

import org.json.JSONObject;

import okhttp3.HttpUrl;

/**
 * Created by peter on 09/11/18.
 */

public class ServicesHelper {
    private static ServicesHelper mInstance;
    private RequestQueue mRequestQueue;

    private static final String BASE_URL = "http://openweathermap.org/data/2.5/weather";


    private <T> void addToRequestQueue(Request<T> req) {
        mRequestQueue.add(req);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    private ServicesHelper(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static synchronized ServicesHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ServicesHelper(context.getApplicationContext());
        }
        return mInstance;
    }


    public void getCurrentWeather(LatLng latLng, final Response.Listener<GetCurrentWeatherResponse> getCurrentWeatherSuccessListener, final Response.ErrorListener getCurrentWeatherErrorListener) {
        try {

            String url = HttpUrl.parse(BASE_URL).newBuilder()
                    .addQueryParameter(Constants.NetworkConstants.LAT_KEYWORD, String.valueOf(latLng.getLatitude()))
                    .addQueryParameter(Constants.NetworkConstants.LNG_KEYWORD, String.valueOf(latLng.getLongitude()))
                    .addQueryParameter(Constants.NetworkConstants.APP_ID_KEYWORD, BuildConfig.API_KEY)
                    .build().toString();


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new JSONObject(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response != null) {
                        GetCurrentWeatherResponse getCurrentWeatherResponse = GsonWrapper.getInstance().getGson().fromJson(response.toString(), GetCurrentWeatherResponse.class);
                        getCurrentWeatherSuccessListener.onResponse(getCurrentWeatherResponse);
                    } else
                        getCurrentWeatherErrorListener.onErrorResponse(new VolleyError());
                }

            }, getCurrentWeatherErrorListener);

            jsonObjectRequest.setTag(Tag.GET_CURRENT_WEATHER);
            addToRequestQueue(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
            getCurrentWeatherErrorListener.onErrorResponse(new VolleyError());
        }
    }

    public enum Tag {
        GET_CURRENT_WEATHER
    }
}
