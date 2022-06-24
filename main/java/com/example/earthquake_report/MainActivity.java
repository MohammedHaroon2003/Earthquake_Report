package com.example.earthquake_report;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager;
import android.content.Loader;
import android.widget.TextView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    private QuakeAdapter mAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private View mLoadingIndicator;

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=20";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mAdapter = new QuakeAdapter(this, R.layout.earthquake_list_item, new ArrayList<EarthQuake>());


        earthquakeListView.setAdapter(mAdapter);

        // get our empty view in a global variable
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        // In our earthquake list view use setEmptyView method and declare which textView needs to visible when list is empty
        earthquakeListView.setEmptyView(mEmptyStateTextView);


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                EarthQuake currentDetails = mAdapter.getItem(position);

                Uri earthQuakeUri = Uri.parse(currentDetails.getUrl());

                Intent url = new Intent(Intent.ACTION_VIEW, earthQuakeUri);
                startActivity(url);
            }
        });


     //geting the request from hardware to work with network using ConnectivityManager it will manage the services
     ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

     NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

     if(networkInfo != null && networkInfo.isConnected()){

         // get a reference to the loader, in order to interact with loaders
         LoaderManager loaderManager = getLoaderManager();
         loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);

     }else{
          mLoadingIndicator = findViewById(R.id.loading_indicator);
          mLoadingIndicator.setVisibility(View.GONE);


          // update empty state with no internet connection text
          mEmptyStateTextView.setText(R.string.no_internet_connection);



     }




    }

    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int i,Bundle bundle){
        return new EarthquakeLoader(this,USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader,List<EarthQuake> earthQuakes){

        // hide loading indicator because the data has been loaded
          mLoadingIndicator = findViewById(R.id.loading_indicator);
          mLoadingIndicator.setVisibility(View.GONE);

          mEmptyStateTextView.setText(R.string.empty_state_text);
        // clear the adapter of previous earthquake data
          mAdapter.clear();

        if(earthQuakes != null && !earthQuakes.isEmpty()){
            mAdapter.addAll(earthQuakes);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader){
        mAdapter.clear();
    }


}

/***
 *******************************************************************************************************
 this is removed because we are no longer using the AsyncTask instead we use AsyncTaskLoader so i just
 comment this lines of code.
 *******************************************************************************************************


 public  class EarthQuakeAsyncTask extends AsyncTask<String,Void, List<EarthQuake>> {

@Override protected List<EarthQuake> doInBackground(String... url){
if(url.length < 1 || url[0] == null){
return null;
}

List<EarthQuake> result = QueryUtils.fetchEarthQuakeData(url[0]);
return result;
}

@Override protected void onPostExecute(List<EarthQuake> data){
mAdapter.clear();

if(data!=null && !data.isEmpty()) {
mAdapter.addAll(data);
}

}

}
 **/