package com.petros.photoweatherapp.photo_full_view;

import android.os.Bundle;

import com.petros.photoweatherapp.common.base.BasePresenter;
import com.petros.photoweatherapp.common.helpers.Constants;

import java.io.File;

/**
 * Created by peter on 10/11/18.
 */

public class PresenterPhotoFullView extends BasePresenter {

    private ViewPhotoFullView viewPhotoFullView;
    private File file;

    public PresenterPhotoFullView(ViewPhotoFullView viewPhotoFullView) {
        this.viewPhotoFullView = viewPhotoFullView;
    }

    void setFile(Bundle bundle) {
        file = (File) bundle.get(Constants.BundleKeys.FILE_KEY);
        viewPhotoFullView.onImageRetrieveSuccess();
    }


    public File getFile() {
        return file;
    }
}
