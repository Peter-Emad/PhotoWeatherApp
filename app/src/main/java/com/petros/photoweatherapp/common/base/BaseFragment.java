package com.petros.photoweatherapp.common.base;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by peter on 09/11/18.
 */

public abstract class BaseFragment extends Fragment {

    protected abstract void initializeViews(View v);

    protected abstract void setListeners();
}
