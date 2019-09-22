package com.example.acer.demoproject.SearchActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Acer on 6/28/2019.
 */

public class DataPackager {
    String query;

    public DataPackager(String query) {
        this.query = query;
    }

    public String packageData(){
        JSONObject jsonObject = new JSONObject();
        StringBuffer stringBuffer = new StringBuffer();

        try{
            jsonObject.put("Query",query);

            Boolean firstValue = true;
            Iterator iterator = jsonObject.keys();

            do{
                String key = iterator.next().toString();
                String value = jsonObject.get(key).toString();

                if(firstValue){
                    firstValue=false;
                }else{
                    stringBuffer.append("&");
                }

                stringBuffer.append(URLEncoder.encode(key,"UTF-8"));
                stringBuffer.append("=");
                stringBuffer.append(URLEncoder.encode(value,"UTF-8"));
            }while(iterator.hasNext());

            return stringBuffer.toString();
        }catch (JSONException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
