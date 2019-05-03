package com.example.acer.demoproject.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.demoproject.AreaOnClick.PlaceDescription;
import com.example.acer.demoproject.AreaOnClick.PlaceList;
import com.example.acer.demoproject.R;

/**
 * Created by Acer on 4/29/2019.
 */

public class PlaceListRecyclerViewAdapter extends RecyclerView.Adapter<PlaceListRecyclerViewAdapter.ImageViewHolder>{

    private int[] images;
    private String[] titles;
    private String[] subtitles;

    public PlaceListRecyclerViewAdapter(int[] images, String[] titles, String[] subtitles) {
        this.images = images;
        this.titles = titles;
        this.subtitles = subtitles;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final int image_id = images[position];
        final String title_name = titles[position];
        final String subtitle_name = subtitles[position];
        holder.images.setImageResource(image_id);
        holder.titles.setText(title_name);
        holder.subtitles.setText(subtitle_name);

        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), PlaceDescription.class);
                intent.putExtra("image_id",image_id);
                intent.putExtra("title_name",title_name);
                intent.putExtra("subtitle_name",subtitle_name);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView images;
        TextView titles;
        TextView subtitles;

        public ImageViewHolder(View itemView){
            super(itemView);
            images = itemView.findViewById(R.id.place_list_imageview);
            titles = itemView.findViewById(R.id.place_list_textview);
            subtitles = itemView.findViewById(R.id.place_list_textview2);
        }
    }
}
