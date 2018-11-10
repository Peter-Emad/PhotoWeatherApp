package com.petros.photoweatherapp.photos_listing;

import android.content.Context;

import com.petros.photoweatherapp.common.helpers.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by peter on 10/11/18.
 */

class PresenterPhotosListing {

    private Context context;
    private ViewPhotosListing viewPhotosListing;
    private List<File> fileList;

    PresenterPhotosListing(Context context, ViewPhotosListing viewPhotosListing) {
        this.context = context;
        this.viewPhotosListing = viewPhotosListing;
        fileList = new ArrayList<>();
    }

    void getCapturedPhotos() {
        viewPhotosListing.showProgress(true);
        File[] subFiles = FileHelper.getImagesInAppDirectory(context);

        if (subFiles != null)
            fileList.addAll(Arrays.asList(subFiles));

        viewPhotosListing.showProgress(false);
        viewPhotosListing.onImagesRetrieved(fileList);
    }

    public List<File> getFileList() {
        return fileList;
    }
}
