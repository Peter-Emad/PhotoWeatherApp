package com.petros.photoweatherapp.photo_capture;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.petros.photoweatherapp.common.base.BasePresenter;
import com.petros.photoweatherapp.common.models.LatLng;
import com.petros.photoweatherapp.common.models.dto.GetCurrentWeatherResponse;
import com.petros.photoweatherapp.common.network.ServicesHelper;

import java.io.File;

/**
 * Created by peter on 10/11/18.
 */

class PresenterPhotoCapture extends BasePresenter {

    private Context context;
    private ViewPhotoCapture viewPhotoCapture;
    private GetCurrentWeatherResponse getCurrentWeatherResponse;
    private File imgFile;

    PresenterPhotoCapture(Context context, ViewPhotoCapture viewPhotoCapture) {
        this.context = context;
        this.viewPhotoCapture = viewPhotoCapture;
    }


    void getCurrentLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        ServicesHelper.getInstance(context).getCurrentWeather(latLng, new Response.Listener<GetCurrentWeatherResponse>() {
            @Override
            public void onResponse(GetCurrentWeatherResponse response) {
                getCurrentWeatherResponse = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error_response", error.getMessage());
            }
        });
    }

    public GetCurrentWeatherResponse getGetCurrentWeatherResponse() {
        return getCurrentWeatherResponse;
    }

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }
}
