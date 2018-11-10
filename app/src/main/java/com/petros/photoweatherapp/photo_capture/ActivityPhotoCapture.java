package com.petros.photoweatherapp.photo_capture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.petros.photoweatherapp.R;
import com.petros.photoweatherapp.common.base.BaseActivity;

/**
 * Created by peter on 10/11/18.
 */

public class ActivityPhotoCapture extends BaseActivity implements FragmentPhotoCapture.FragmentPhotoCaptureInteractions {


    private Toolbar photoCaptureToolbar;


    public static Intent createIntent(Context context) {
        return new Intent(context, ActivityPhotoCapture.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        initializeViews();
        setToolbar(photoCaptureToolbar, getString(R.string.capture_image), true, false);
        replaceFragment(FragmentPhotoCapture.newInstance(), R.id.flFragmentContainer);
    }

    @Override
    protected void initializeViews() {
        photoCaptureToolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onSaveImageSuccess() {
        setResult(RESULT_OK);
        finish();
    }
}
