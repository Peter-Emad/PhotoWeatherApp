package com.petros.photoweatherapp.photo_full_view;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.petros.photoweatherapp.R;
import com.petros.photoweatherapp.common.base.BaseFragment;
import com.petros.photoweatherapp.common.helpers.Constants;

import java.io.File;

/**
 * Created by peter on 10/11/18.
 */

public class FragmentPhotoFullView extends BaseFragment implements ViewPhotoFullView {

    private ImageView imgFullView;
    private PresenterPhotoFullView presenterPhotoFullView;

    public static FragmentPhotoFullView newInstance(File file) {
        FragmentPhotoFullView fragmentPhotoFullView = new FragmentPhotoFullView();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BundleKeys.FILE_KEY, file);
        fragmentPhotoFullView.setArguments(bundle);
        return fragmentPhotoFullView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenterPhotoFullView = new PresenterPhotoFullView(this);
        presenterPhotoFullView.setFile(getArguments());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_view, container, false);
        initializeViews(view);
        setListeners();
        return view;
    }

    @Override
    protected void initializeViews(View v) {
        imgFullView = v.findViewById(R.id.imgFullView);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onImageRetrieveSuccess() {
        if (presenterPhotoFullView.getFile() != null)
            imgFullView.setImageURI(Uri.fromFile(presenterPhotoFullView.getFile()));
    }
}
