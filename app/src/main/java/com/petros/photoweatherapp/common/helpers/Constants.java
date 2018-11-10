package com.petros.photoweatherapp.common.helpers;

/**
 * Created by peter on 09/11/18.
 */

public class Constants {

    public static final class NetworkConstants {
        public static final String LAT_KEYWORD = "lat";
        public static final String LNG_KEYWORD = "lon";
        public static final String APP_ID_KEYWORD = "appid";
    }

    public static class ActivityRequestCodes {
        public static final int IMAGE_CAPTURE_REQUEST_CODE = 1000;
        public static final int CAPTURE_NEW_WEATHER_IMAGE = 1001;
    }


    public static class BundleKeys {
        public static final String PACKAGE_KEY = "package";
        public static final String FILE_KEY = "file_key";
    }

    public static class ImagesConstants {
        public static final String GIF_EXT = "gif";
        public static final String PNG_EXT = "png";
        public static final String BMP_EXT = "bmp";
        public static final String JPG_EXT = "jpg";
        public static final String JPEG_EXT = "jpeg";
        public static final String SAVE_IMAGE_JPG_EXT = ".jpg";
        public static final String SAVE_IMAGE_DATE_FORMAT = "yyyyMMdd_HHmmss";
        public static final String SAVE_IMAGE_IMG_KEYWORD = "IMG_";


    }
}
