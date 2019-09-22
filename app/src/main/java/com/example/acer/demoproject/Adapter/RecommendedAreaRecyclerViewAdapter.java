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
 * Created by Acer on 4/25/2019.
 */

public class RecommendedAreaRecyclerViewAdapter extends RecyclerView.Adapter<RecommendedAreaRecyclerViewAdapter.ImageViewHolder> {

    private static final String TAG = "RecommendedAdapter";  // for debugging

    private int[] images;
    private String[] titles;
    private Double[] ratings;

    public RecommendedAreaRecyclerViewAdapter(int[] images, String[] titles, Double[] ratings) {
        this.images = images;
        this.titles = titles;
        this.ratings = ratings;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder:called");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_arealist,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder:called");
        int image_id = images[position];
        final String title_name = titles[position];
        final Double rating_value = ratings[position];
        holder.image.setImageResource(image_id);
        holder.title.setText(title_name);
        holder.rate.setText(String.valueOf(rating_value));

//        final PostAreaRecyclerView p = postList.get(position);
//
//        Glide.with(p.mcontext)
//                .asBitmap()
//                .load(mImageUrls.get(position))
//                .into(holder.circleImage);
//
//        holder.name.setText(p.mNames);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: clicked on an image" +title_name);
                Toast.makeText(v.getContext(),title_name,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), PlaceDescription.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title,rate;

        public ImageViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.areaImageView);
            title = itemView.findViewById(R.id.areaTextView);
            rate = itemView.findViewById(R.id.ratingTextView);
        }
    }
}
