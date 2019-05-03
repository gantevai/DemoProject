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

/**
 * Created by Acer on 4/30/2019.
 */

public class PlaceDescriptionSliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    //Arrrays
    private int[] slideimages = {
            R.drawable.religious,
            R.drawable.historical,
            R.drawable.natural,
            R.drawable.adventurous
    };

    public PlaceDescriptionSliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slideimages.length;
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

        slideImageView.setImageResource(slideimages[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout) object);

    }


}
