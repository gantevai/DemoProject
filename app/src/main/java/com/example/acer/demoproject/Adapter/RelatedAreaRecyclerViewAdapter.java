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
import com.example.acer.demoproject.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Acer on 5/1/2019.
 */

public class RelatedAreaRecyclerViewAdapter extends RecyclerView.Adapter<RelatedAreaRecyclerViewAdapter.ImageViewHolder>{
    private static final String TAG = "RecommendedAdapter";  // for debugging

    private int[] images;
    private String[] titles;

    public RelatedAreaRecyclerViewAdapter(int[] images, String[] titles) {
        this.images = images;
        this.titles = titles;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder:called");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_arealist,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder:called");
        int image_id = images[position];
        String image_url= "http://pasang1422.000webhostapp.com/place_images/"+String.valueOf(image_id)+".png";
        final String title_name = titles[position];
        Picasso.get().load(image_url).into(holder.getImage());
        holder.title.setText(title_name);

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
        TextView title;

        public ImageViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.areaImageView);
            title = itemView.findViewById(R.id.areaTextView);
        }

        public ImageView getImage() {
            return this.image;
        }
    }
}

