package com.petros.photoweatherapp.photos_listing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.petros.photoweatherapp.R;
import com.petros.photoweatherapp.common.base.BaseActivity;

/**
 * Created by peter on 10/11/18.
 */

public class ActivityPhotosListing extends BaseActivity {

    private Toolbar photosListingToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        initializeViews();
        setToolbar(photosListingToolbar, getString(R.string.weather_photos), false, false);
        replaceFragment(FragmentPhotosListing.newInstance(), R.id.flFragmentContainer);
    }

    @Override
    protected void initializeViews() {
        photosListingToolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void setListeners() {

    }
}
