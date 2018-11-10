package com.petros.photoweatherapp.photo_capture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.petros.photoweatherapp.R;
import com.petros.photoweatherapp.common.base.PermissionsHandlerFragment;
import com.petros.photoweatherapp.common.base.PermissionsListener;
import com.petros.photoweatherapp.common.helpers.Constants;
import com.petros.photoweatherapp.common.helpers.Utilities;
import com.petros.photoweatherapp.common.network.ServicesHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by peter on 10/11/18.
 */

public class FragmentPhotoCapture extends PermissionsHandlerFragment implements ViewPhotoCapture, PermissionsListener {

    private Context context;
    private PresenterPhotoCapture presenterPhotoCapture;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private PhotoEditorView photoEditorView;
    private PhotoEditor photoEditor;
    private ProgressBar pbPhotoCapture;
    private FragmentPhotoCaptureInteractions fragmentPhotoCaptureInteractions;

    public static FragmentPhotoCapture newInstance() {
        return new FragmentPhotoCapture();
    }

    interface FragmentPhotoCaptureInteractions {

        void onSaveImageSuccess();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentPhotoCaptureInteractions = (FragmentPhotoCaptureInteractions) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        setHasOptionsMenu(true);

        presenterPhotoCapture = new PresenterPhotoCapture(context, this);

        photoEditor = new PhotoEditor.Builder(context, photoEditorView).build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        checkPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION);

        startCamera();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_capture, container, false);
        initializeViews(view);
        setListeners();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                saveImageToSamePath();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServicesHelper.getInstance(context).getRequestQueue().cancelAll(ServicesHelper.Tag.GET_CURRENT_WEATHER);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentPhotoCaptureInteractions = null;
    }

    @Override
    protected void initializeViews(View v) {
        photoEditorView = v.findViewById(R.id.photoEditorView);
        pbPhotoCapture = v.findViewById(R.id.progress_bar);
    }

    @Override
    protected void setListeners() {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionGranted() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                    presenterPhotoCapture.getCurrentLocation(location);
                else
                    requestLocationUpdates();

            }
        });

    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        presenterPhotoCapture.getCurrentLocation(location);

                        if (fusedLocationProviderClient != null)
                            fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onPermissionDenied(boolean userCheckedNeverAskAgain) {
        Utilities.showBasicDialog(context, getString(R.string.permissions_needed)
                , getString(R.string.permissions_needed_message)
                , userCheckedNeverAskAgain ? getString(R.string.settings) : getString(R.string.ok), getString(R.string.cancel)
                , userCheckedNeverAskAgain ? positiveCheckForPermissionAfterDontAsk : positiveCheckForPermission
                , cancelDialog);
    }

    private void startCamera() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File out = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        out = new File(out, Constants.ImagesConstants.SAVE_IMAGE_IMG_KEYWORD + new SimpleDateFormat(Constants.ImagesConstants.SAVE_IMAGE_DATE_FORMAT, Locale.ENGLISH).format(new Date()) + Constants.ImagesConstants.SAVE_IMAGE_JPG_EXT);
        presenterPhotoCapture.setImgFile(out);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
        startActivityForResult(i, Constants.ActivityRequestCodes.IMAGE_CAPTURE_REQUEST_CODE);
    }

    private void addTextToImage() {
        StringBuilder stringBuilder = new StringBuilder();

        if (presenterPhotoCapture.getGetCurrentWeatherResponse() != null) {
            stringBuilder.append(presenterPhotoCapture.getGetCurrentWeatherResponse().getName())
                    .append(System.lineSeparator())
                    .append(presenterPhotoCapture.getGetCurrentWeatherResponse().getWeather().get(0).getDescription())
                    .append(System.lineSeparator())
                    .append(presenterPhotoCapture.getGetCurrentWeatherResponse().getMain().getTemp())
                    .append(" \u2103");
            photoEditor.addText(stringBuilder.toString(), context.getResources().getColor(android.R.color.white));
        }
    }

    private void saveImageToSamePath() {
        showProgress(true);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            photoEditor.saveAsFile(presenterPhotoCapture.getImgFile().getPath(), onSaveListener);
    }

    private void showProgress(boolean show) {
        pbPhotoCapture.setVisibility(show ? View.VISIBLE : View.GONE);
        photoEditorView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private PhotoEditor.OnSaveListener onSaveListener = new PhotoEditor.OnSaveListener() {
        @Override
        public void onSuccess(@NonNull String imagePath) {
            showProgress(false);
            if (fragmentPhotoCaptureInteractions != null)
                fragmentPhotoCaptureInteractions.onSaveImageSuccess();
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            showProgress(false);
            Utilities.showShortToast(context, exception.getMessage());
        }
    };

    private DialogInterface.OnClickListener positiveCheckForPermission = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            checkPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    };

    private DialogInterface.OnClickListener positiveCheckForPermissionAfterDontAsk = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(Constants.BundleKeys.PACKAGE_KEY, context.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    };

    private DialogInterface.OnClickListener cancelDialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.ActivityRequestCodes.IMAGE_CAPTURE_REQUEST_CODE) {
            if (presenterPhotoCapture.getImgFile().exists()) {
                photoEditorView.getSource().setImageURI(Uri.fromFile(presenterPhotoCapture.getImgFile()));
                addTextToImage();
            }
        }
    }

}
