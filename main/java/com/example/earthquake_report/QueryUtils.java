package com.example.earthquake_report;




import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public final class QueryUtils {


     private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    private static URL createUrl(String stringUrl){
       URL url = null;

       try{
           url = new URL(stringUrl);
       }catch(MalformedURLException e) {
           Log.e(LOG_TAG,"Problem building the url:" + e);
       }

       return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error Response Code:"+urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG,"Problem recieving the JSON result",e);
        }finally{
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

     private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream!=null) {
              InputStreamReader inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-8"));
              BufferedReader reader = new BufferedReader(inputStreamReader);
               String line = reader.readLine();
              while(line!=null){
                   output.append(line);
                   line = reader.readLine();
              }
        }
        return output.toString();
     }


   public static List<EarthQuake> fetchEarthQuakeData(String requestUrl){


        /**
         * thread will not work for 2000 millisecond so we can able to see the loading indicator.
         * just for testing so i commented this lines of code.
           try{
               Thread.sleep(2000); // milliseconds
           }catch(InterruptedException e){
               e.printStackTrace();
           }
         **/

        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
           Log.e(LOG_TAG,"Problem making htttp request:",e);
        }

        List<EarthQuake> earthquakes = extractFeatureFromJson(jsonResponse);

        return earthquakes;
   }




    private static List<EarthQuake> extractFeatureFromJson(String earthquakeJson){

        if(TextUtils.isEmpty(earthquakeJson)){
            return null;
        }

        List<EarthQuake> earthquakeList = new ArrayList<>();

        try{
            JSONObject root = new JSONObject(earthquakeJson);
            JSONArray featuresArray = root.optJSONArray("features");

            for(int i=0; i<featuresArray.length(); i++){

                JSONObject featuresObjects = featuresArray.getJSONObject(i);
                JSONObject properties = featuresObjects.getJSONObject("properties");

                double magnitude = properties.optDouble("mag");
                String place = properties.optString("place");
                Long time = properties.optLong("time");
                String url = properties.optString("url");

                earthquakeList.add(new EarthQuake(magnitude,place,time,url));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return earthquakeList;
    }




}