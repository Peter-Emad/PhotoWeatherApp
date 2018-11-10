package com.petros.photoweatherapp.photos_listing;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.petros.photoweatherapp.R;
import com.petros.photoweatherapp.common.base.PermissionsHandlerFragment;
import com.petros.photoweatherapp.common.base.PermissionsListener;
import com.petros.photoweatherapp.common.helpers.Constants;
import com.petros.photoweatherapp.common.helpers.Utilities;
import com.petros.photoweatherapp.photo_capture.ActivityPhotoCapture;
import com.petros.photoweatherapp.photo_full_view.ActivityPhotoFullView;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by peter on 10/11/18.
 */

public class FragmentPhotosListing extends PermissionsHandlerFragment implements ViewPhotosListing, PermissionsListener, AdapterPhotosListing.AdapterPhotosListingInteractions {

    private Context context;
    private RecyclerView rvPhotos;
    private PresenterPhotosListing presenterPhotosListing;
    private ProgressBar pbPhotosListing;
    private ImageView imgError;
    private TextView txtError;
    private ConstraintLayout clErrorLayout;
    private AdapterPhotosListing adapterOrderImages;

    public static FragmentPhotosListing newInstance() {
        return new FragmentPhotosListing();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        presenterPhotosListing = new PresenterPhotosListing(context, this);
        presenterPhotosListing.getCapturedPhotos();
        initRv();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos_listing, container, false);
        initializeViews(view);
        setListeners();
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_capture_image, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_capture_image:
                checkPermissions(context, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initializeViews(View v) {
        rvPhotos = v.findViewById(R.id.rvPhotos);
        pbPhotosListing = v.findViewById(R.id.progress_bar);
        imgError = v.findViewById(R.id.imgError);
        txtError = v.findViewById(R.id.txtError);
        clErrorLayout = v.findViewById(R.id.clErrorLayout);
    }

    @Override
    protected void setListeners() {

    }


    @Override
    public void onImagesRetrieved(List<File> fileList) {
        if (fileList.size() > 0)
            handleProgressVisibility(false, true, false, null, "");
        else
            handleProgressVisibility(false, false, true, getResources().getDrawable(R.drawable.ic_no_photos),
                    context.getString(R.string.no_photos));

    }

    @Override
    public void showProgress(boolean show) {
        handleProgressVisibility(show, false, false, null, "");
    }

    @Override
    public void onPermissionGranted() {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager != null) {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Utilities.showBasicDialog(context, getString(R.string.enable_gps)
                        , getString(R.string.enable_gps_message)
                        , getString(R.string.settings), getString(R.string.cancel)
                        , positiveEnableGPS
                        , cancelDialog);
            } else
                startActivityForResult(ActivityPhotoCapture.
                        createIntent(context), Constants.ActivityRequestCodes.CAPTURE_NEW_WEATHER_IMAGE);
        } else
            Utilities.showShortToast(context, getString(R.string.something_went_wrong));

    }

    @Override
    public void onPermissionDenied(boolean userCheckedNeverAskAgain) {
        Utilities.showBasicDialog(context, getString(R.string.permissions_needed)
                , getString(R.string.permissions_needed_message)
                , userCheckedNeverAskAgain ? getString(R.string.settings) : getString(R.string.ok), getString(R.string.cancel)
                , userCheckedNeverAskAgain ? positiveCheckForPermissionAfterDontAsk : positiveCheckForPermission
                , cancelDialog);
    }


    @Override
    public void onImageFileClick(File file) {
        ActivityPhotoFullView.startActivity(context, file);
    }

    private void initRv() {
        adapterOrderImages = new AdapterPhotosListing(presenterPhotosListing.getFileList(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvPhotos.setLayoutManager(linearLayoutManager);
        rvPhotos.setAdapter(adapterOrderImages);
    }

    private DialogInterface.OnClickListener positiveEnableGPS = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    };

    private DialogInterface.OnClickListener positiveCheckForPermission = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            checkPermissions(context, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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

    private void handleProgressVisibility(boolean showProgress, boolean showList, boolean showError, Drawable errorImage, String errorText) {
        pbPhotosListing.setVisibility(showProgress ? View.VISIBLE : View.GONE);
        rvPhotos.setVisibility(showList ? View.VISIBLE : View.GONE);
        clErrorLayout.setVisibility(showError ? View.VISIBLE : View.GONE);
        imgError.setBackground(errorImage);
        txtError.setText(errorText);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.ActivityRequestCodes.CAPTURE_NEW_WEATHER_IMAGE) {
                presenterPhotosListing.getFileList().clear();
                presenterPhotosListing.getCapturedPhotos();
                adapterOrderImages.notifyDataSetChanged();
            }

        }
    }

}
