package com.petros.photoweatherapp.photos_listing;

import java.io.File;
import java.util.List;

/**
 * Created by peter on 10/11/18.
 */

public interface ViewPhotosListing {

    void onImagesRetrieved(List<File> fileList);

    void showProgress(boolean show);
}
