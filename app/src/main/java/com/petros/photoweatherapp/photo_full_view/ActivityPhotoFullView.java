package com.petros.photoweatherapp.photo_full_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.petros.photoweatherapp.R;
import com.petros.photoweatherapp.common.base.BaseActivity;
import com.petros.photoweatherapp.common.helpers.Constants;
import com.petros.photoweatherapp.photo_capture.ActivityPhotoCapture;
import com.petros.photoweatherapp.photo_capture.FragmentPhotoCapture;

import java.io.File;

/**
 * Created by peter on 10/11/18.
 */

public class ActivityPhotoFullView extends BaseActivity {

    private Toolbar photoFullViewToolbar;


    public static void startActivity(Context context, File file) {
        Intent intent = new Intent(context, ActivityPhotoFullView.class);
        intent.putExtra(Constants.BundleKeys.FILE_KEY, file);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        initializeViews();
        setToolbar(photoFullViewToolbar, getString(R.string.full_view), true, false);
        File file = null;
        if (getIntent().getExtras() != null)
            file = (File) getIntent().getExtras().get(Constants.BundleKeys.FILE_KEY);
        if (file != null)
            replaceFragment(FragmentPhotoFullView.newInstance(file), R.id.flFragmentContainer);
    }

    @Override
    protected void initializeViews() {
        photoFullViewToolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void setListeners() {

    }
}
