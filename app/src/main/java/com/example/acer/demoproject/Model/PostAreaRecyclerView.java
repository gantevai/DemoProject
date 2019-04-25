package com.example.acer.demoproject.Model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Acer on 4/25/2019.
 */

public class PostAreaRecyclerView {
     ArrayList<String> mNames = new ArrayList<>();
     ArrayList<String> mImageUrls = new ArrayList<>();
     Context mcontext;

    public PostAreaRecyclerView(ArrayList<String> mNames, ArrayList<String> mImageUrls, Context mcontext) {
        this.mNames = mNames;
        this.mImageUrls = mImageUrls;
        this.mcontext = mcontext;
    }
}
// aile yesko kaam xaina