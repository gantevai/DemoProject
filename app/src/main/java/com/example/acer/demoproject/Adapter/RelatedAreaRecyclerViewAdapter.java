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

import com.example.acer.demoproject.AreaOnClick.PlaceDescription;
import com.example.acer.demoproject.Model.RecommendedPlaces;
import com.example.acer.demoproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Acer on 5/1/2019.
 */

public class RelatedAreaRecyclerViewAdapter extends RecyclerView.Adapter<RelatedAreaRecyclerViewAdapter.ImageViewHolder>{
    private static final String TAG = "RecommendedAdapter";  // for debugging
    private List<RecommendedPlaces> places;
    private Context context;

    public RelatedAreaRecyclerViewAdapter(List<RecommendedPlaces> places, Context context) {
        this.places = places;
        this.context= context;
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
        final RecommendedPlaces recPlaces = places.get(position);
        Picasso.get().load(recPlaces.getImage_Url())
                .into(holder.getImage());
        holder.title.setText(recPlaces.getTitle());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendedPlaces place1 = places.get(position);
                Intent intent = new Intent(v.getContext(), PlaceDescription.class);
                intent.putExtra("image_url", place1.getImage_Url());
                intent.putExtra("title_name", place1.getTitle());
                intent.putExtra("place_id", String.valueOf(place1.getPlace_id()));
                intent.putExtra("place_description", place1.getPlace_description());
                intent.putExtra("place_type", place1.getPlace_type());
                intent.putExtra("place_lat", String.valueOf(place1.getPlaceLat()));
                intent.putExtra("place_long", String.valueOf(place1.getPlaceLong()));
                Log.d(TAG,"onClick: clicked on an image" +recPlaces.placeName);
                Toast.makeText(v.getContext(),recPlaces.placeName,Toast.LENGTH_SHORT).show();
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
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

