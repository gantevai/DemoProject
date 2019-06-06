package com.example.acer.demoproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


//import com.bumptech.glide.Glide;
import com.example.acer.demoproject.AreaOnClick.PlaceList;
import com.example.acer.demoproject.MainActivity;
import com.example.acer.demoproject.Model.PostAreaRecyclerView;
import com.example.acer.demoproject.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Acer on 4/25/2019.
 */

public class AreaRecyclerViewAdapter extends RecyclerView.Adapter<AreaRecyclerViewAdapter.ImageViewHolder> {

    private static final String TAG = "AreaRecyclerViewAdapter";  // for debugging

    private int[] images;
    private String[] titles;

    public AreaRecyclerViewAdapter(int[] images,String[] titles) {
        this.images = images;
        this.titles = titles;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder:called");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.area_list,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder:called");
        int image_id = images[position];
        final String title_name = titles[position];
        holder.circleImage.setImageResource(image_id);
        holder.title.setText(title_name);

//        final PostAreaRecyclerView p = postList.get(position);
//
//        Glide.with(p.mcontext)
//                .asBitmap()
//                .load(mImageUrls.get(position))
//                .into(holder.circleImage);
//
//        holder.name.setText(p.mNames);

        holder.circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: clicked on an image" +title_name);
                Toast.makeText(v.getContext(),title_name,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), PlaceList.class);
                intent.putExtra("area_category",title_name);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView circleImage;
        TextView title;

        public ImageViewHolder(View itemView){
            super(itemView);
            circleImage = itemView.findViewById(R.id.areaImageView);
            title = itemView.findViewById(R.id.areaTextView);
        }
    }
}
