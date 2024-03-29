package com.example.earthquake_report;

import android.content.AsyncTaskLoader;
import android.content.Context;



import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthQuake>> {

    private String mUrl;

    public EarthquakeLoader(Context context,String url){
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<EarthQuake> loadInBackground(){
        if(mUrl == null){
            return null;
        }

        //perform the network request, parse the response, and extract a list of earthquake
        List<EarthQuake> earthQuakes = QueryUtils.fetchEarthQuakeData(mUrl);
        return earthQuakes;
    }


}
