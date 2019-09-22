package com.example.acer.demoproject.SearchActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.acer.demoproject.Adapter.SearchListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Acer on 6/28/2019.
 */

public class Parser extends AsyncTask<Void,Void,Integer> {
    Context context;
    String data;
    ListView listView;
    int userID;

    ArrayList<String> names = new ArrayList<>();

    public Parser(Context context, String data, ListView listView,int userID) {
        this.context = context;
        this.data = data;
        this.listView = listView;
        this.userID = userID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return (this.parse());
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if(integer==1){
            //Bind to ListView
            SearchListAdapter adapter = new SearchListAdapter(names,context,userID);
            listView.setAdapter(adapter);
        }else{
            Toast.makeText(context,"Unable to Parse",Toast.LENGTH_SHORT).show();
        }
    }

    private int parse(){
        try{
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = null;

            names.clear();
            for(int i=0;i<jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("placeName");
                names.add(name);
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
