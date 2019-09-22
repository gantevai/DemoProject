package com.example.acer.demoproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.acer.demoproject.AreaOnClick.PlaceDescription;
import com.example.acer.demoproject.AreaOnClick.PlaceList;
import com.example.acer.demoproject.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Acer on 4/29/2019.
 */

public class PlaceListRecyclerViewAdapter extends RecyclerView.Adapter<PlaceListRecyclerViewAdapter.ImageViewHolder> {

    ArrayList<String> imagesUrl;
    private String[] titles,subtitles,placeDescription,placeType;
    private Double[] placeLat,placeLong,ratingValue;
    private int[] place_id;
    private int userID;
    Context context;

    public PlaceListRecyclerViewAdapter(ArrayList<String> imagesUrl, String[] titles,
                                        String[] subtitles, int[] place_id,Double[] placeLat, Double[] placeLong, String[] placeDescription,
                                        String[] placeType,Double[] ratingValue,int userID, Context context) {
        this.imagesUrl = imagesUrl;
        this.titles = titles;
        this.subtitles = subtitles;
        this.place_id = place_id;
        this.placeLat = placeLat;
        this.placeLong = placeLong;
        this.placeDescription = placeDescription;
        this.placeType = placeType;
        this.context = context;
        this.ratingValue= ratingValue;
        this.userID = userID;
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        final String image_url = imagesUrl.get(position);
        final String title_name = titles[position];
        final String subtitle_name = subtitles[position];
        final int placeID = place_id[position];
        final String place_description = placeDescription[position];
        final String place_type = placeType[position];
        final Double place_lat = placeLat[position];
        final Double place_long = placeLong[position];
        final Double rating_value = ratingValue[position];

        Picasso.get().load(image_url).into(holder.getImage());
        holder.titles.setText(title_name);
        holder.subtitles.setText(subtitle_name);
        holder.rating.setRating(Float.parseFloat(String.valueOf(rating_value)));

        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), PlaceDescription.class);
                intent.putExtra("image_url", image_url);
                intent.putExtra("title_name", title_name);
                intent.putExtra("subtitle_name", subtitle_name);
                intent.putExtra("place_id", String.valueOf(placeID));
                intent.putExtra("place_lat", String.valueOf(place_lat));
                intent.putExtra("place_long", String.valueOf(place_long));
                intent.putExtra("place_description", place_description);
                intent.putExtra("place_type", place_type);
                intent.putExtra("rating_value", String.valueOf(rating_value));
                intent.putExtra("userID", String.valueOf(userID));

                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != imagesUrl ? imagesUrl.size() : 0);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView titles;
        TextView subtitles;
        RatingBar rating;

        public ImageViewHolder(View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.place_list_imageview);
            titles = itemView.findViewById(R.id.place_list_textview);
            subtitles = itemView.findViewById(R.id.place_list_textview2);
            rating = itemView.findViewById(R.id.place_list_ratingbar);
        }

        public ImageView getImage() {
            return this.images;
        }
    }
}
