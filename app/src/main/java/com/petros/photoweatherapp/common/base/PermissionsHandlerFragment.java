package com.petros.photoweatherapp.common.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by peter on 10/11/18.
 */

public abstract class PermissionsHandlerFragment extends BaseFragment {
    private PermissionsListener permissionsListener = (PermissionsListener) this;
    private static final int MY_PERMISSIONS_REQUEST = 1000;

    public void checkPermissions(Context context, String... permissions) {
        ArrayList<String> notGrantedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermissions.add(permission);
            }
        }
        if (notGrantedPermissions.size() == 0) {
            permissionsListener.onPermissionGranted();
        } else {
            requestPermissions(notGrantedPermissions.toArray(new String[notGrantedPermissions.size()]), MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                boolean permissionsGranted = true;
                boolean userCheckedNeverAskAgain = false;
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            permissionsGranted = false;
                            break;
                        }
                    }
                }

                for (String permission : permissions) {
                    if (!shouldShowRequestPermissionRationale(permission)) {
                        userCheckedNeverAskAgain = true;
                        break;
                    }
                }
                if (permissionsGranted)
                    permissionsListener.onPermissionGranted();
                else
                    permissionsListener.onPermissionDenied(userCheckedNeverAskAgain);
            }
            break;
        }
    }
}
