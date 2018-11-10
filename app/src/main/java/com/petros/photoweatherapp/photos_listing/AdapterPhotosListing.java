package com.petros.photoweatherapp.photos_listing;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.petros.photoweatherapp.R;

import java.io.File;
import java.util.List;

/**
 * Created by peter on 10/11/18.
 */

public class AdapterPhotosListing extends RecyclerView.Adapter<AdapterPhotosListing.MyViewHolder> {
    private List<File> fileList;
    private AdapterPhotosListingInteractions adapterPhotosListingInteractions;


    interface AdapterPhotosListingInteractions {
        void onImageFileClick(File file);
    }

    public AdapterPhotosListing(List<File> fileList, AdapterPhotosListingInteractions adapterPhotosListingInteractions) {
        this.fileList = fileList;
        this.adapterPhotosListingInteractions = adapterPhotosListingInteractions;
    }

    @NonNull
    @Override
    public AdapterPhotosListing.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.row_weather_image, parent, false);
        return new AdapterPhotosListing.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPhotosListing.MyViewHolder myViewHolder, int position) {

        myViewHolder.imgWeather.setImageURI(Uri.fromFile(fileList.get(position)));
        myViewHolder.setListeners();

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgWeather;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWeather = itemView.findViewById(R.id.imgWeather);

        }

        public void setListeners() {
            imgWeather.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (adapterPhotosListingInteractions != null)
                        adapterPhotosListingInteractions.onImageFileClick(fileList.get(getAdapterPosition()));
                }
            });
        }
    }


}
