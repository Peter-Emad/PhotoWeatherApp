package com.petros.photoweatherapp.common.helpers;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by peter on 10/11/18.
 */

public class FileHelper {


    public static File[] getImagesInAppDirectory(Context context) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (dir != null)
            return dir.listFiles(imagesFileNameFilter());
        else
            return null;
    }

    private static FilenameFilter imagesFileNameFilter() {
        final String[] EXTENSIONS = new String[]{Constants.ImagesConstants.GIF_EXT, Constants.ImagesConstants.PNG_EXT
                , Constants.ImagesConstants.BMP_EXT, Constants.ImagesConstants.JPG_EXT, Constants.ImagesConstants.JPEG_EXT};

        return new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                for (final String ext : EXTENSIONS) {
                    if (name.endsWith("." + ext)) {
                        return (true);
                    }
                }
                return (false);
            }
        };
    }
}
