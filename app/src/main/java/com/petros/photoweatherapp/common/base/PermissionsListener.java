package com.petros.photoweatherapp.common.base;

/**
 * Created by peter on 10/11/18.
 */

public interface PermissionsListener {

    void onPermissionGranted();

    void onPermissionDenied(boolean userCheckedNeverAskAgain);

}
