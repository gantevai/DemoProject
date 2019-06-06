package com.example.acer.demoproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.acer.demoproject.AreaOnClick.PlaceDescription;
import com.example.acer.demoproject.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Acer on 4/30/2019.
 */

public class PlaceDescriptionSliderAdapter extends PagerAdapter {

    Context context;
    private static final String IMAGE_URL = "http://pasang1422.000webhostapp.com/place_images/";

    LayoutInflater layoutInflater;
    int image_id;
    String[] sliderImageUrls = new String[3];

    public void setSliderImageUrls() {
        sliderImageUrls[0] = IMAGE_URL+String.valueOf(image_id)+".png";
        sliderImageUrls[1] = IMAGE_URL+String.valueOf(image_id)+"_1.png";
        sliderImageUrls[2] = IMAGE_URL+String.valueOf(image_id)+"_2.png";
    }

    //Arrrays
    private int[] slideimages = {
            R.drawable.religious,
            R.drawable.historical,
            R.drawable.natural,
            R.drawable.adventurous
    };

    public PlaceDescriptionSliderAdapter(int place_id,Context context) {
        this.context = context;
        this.image_id = place_id;
    }

    @Override
    public int getCount() {
        return sliderImageUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.place_description_slidelayout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.place_description_slide_image);

        setSliderImageUrls();

        slideImageView.setImageResource(slideimages[position]);
//        Picasso.get().load(image_url).into(holder.getImage());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout) object);

    }


}
