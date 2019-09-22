package com.example.acer.demoproject.SearchActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by Acer on 6/28/2019.
 */

public class SenderReceiver extends AsyncTask<Void,Void,String> {
    Context context;
    String urlAddress;
    String query;
    ListView listView;
    ImageView noDataImageView, noNetworkImageView;
    ProgressDialog progressDialog;
    int userID;

    public SenderReceiver(Context context, int userID, String urlAddress, String query, ListView listView, ImageView...imageViews) {
        this.context = context;
        this.userID = userID;
        this.urlAddress = urlAddress;
        this.query = query;
        this.listView = listView;
        this.noDataImageView = imageViews[0];
        this.noNetworkImageView = imageViews[1];
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Search");
        progressDialog.setMessage("Searching...Please Wait");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return this.sendAndReceive();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        progressDialog.dismiss();

        //SET LISTVIEW TO EMPTY OR RESET LISTVIEW
        listView.setAdapter(null);

        if(s != null){
            if(!s.contains("null")){
                Parser parser = new Parser(context,s,listView,userID);
                parser.execute();
            }else{
                noNetworkImageView.setVisibility(View.INVISIBLE);
                noDataImageView.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "No such data", Toast.LENGTH_SHORT).show();
            }
        }else{
            noNetworkImageView.setVisibility(View.INVISIBLE);
            noDataImageView.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "No network", Toast.LENGTH_SHORT).show();
            listView.setAdapter(null);
        }
    }

    private String sendAndReceive(){
        HttpURLConnection con = Connector.connect(urlAddress);
        if(con==null){
            return null;
        }

        try{
            OutputStream outputStream = con.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(new DataPackager(query).packageData());

            bufferedWriter.flush();

            //RELEASE RESOURCES
            bufferedWriter.close();
            outputStream.close();

            //CHECK IF THERE IS SOME RESPONSE ??
            int responseCode = con.getResponseCode();

            //Decode
            if(responseCode==con.HTTP_OK){
                //RETURN SOME DATA
                InputStream inputStream = con.getInputStream();

                //READ DATA
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer response = new StringBuffer();

                if(bufferedReader!=null){
                    while((line=bufferedReader.readLine()) != null){
                        response.append(line+"\n");
                    }
                }else{
                    return null;
                }
                return response.toString();

            }else{
                return String.valueOf(responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
