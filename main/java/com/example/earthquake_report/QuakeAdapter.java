package com.example.earthquake_report;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuakeAdapter extends ArrayAdapter<EarthQuake> {


    public QuakeAdapter(Context context, int resource, List<EarthQuake> quakeList){
        super(context,resource,quakeList);
    }


    static final String LOCATION_SEPERATOR = " of ";

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item,parent,false);
        }

        EarthQuake currentDetail = getItem(position);

        TextView magnitude = listView.findViewById(R.id.text_magnitude);

        String formattedMagnitude = formatMagnitude(currentDetail.getMagnitude());
        magnitude.setText(formattedMagnitude);

        GradientDrawable magnitudeCircle =(GradientDrawable) magnitude.getBackground();

        int magnitudeColor = getMagnitudeColor(currentDetail.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        String originalLocation = currentDetail.getLocation();
        String location_offset;
        String primary_Location;



        if(originalLocation.contains(LOCATION_SEPERATOR)){
            String[] locationArray = originalLocation.split(LOCATION_SEPERATOR);
            location_offset = locationArray[0] + LOCATION_SEPERATOR;
            primary_Location = locationArray[1];



        }else{
            location_offset = "Near the ";
            primary_Location = originalLocation;
        }

        TextView locationOffset = listView.findViewById(R.id.location_offset);
        locationOffset.setText(location_offset);

        TextView primaryLocation = listView.findViewById(R.id.primary_location);
        primaryLocation.setText(primary_Location);




        Date dateObject = new Date(currentDetail.getTime());

        TextView date = listView.findViewById(R.id.text_date);

        String formattedDate = formatDate(dateObject);
        date.setText(formattedDate);

        TextView time = listView.findViewById(R.id.text_time);

        String formattedTime = formatTime(dateObject);
        time.setText(formattedTime);


        return listView;
    }

    public int getMagnitudeColor(double magnitude){

        int magnitudeResourceColor;
        int magnitudeFloor =(int) Math.floor(magnitude);

        switch(magnitudeFloor){
            case 1:
                magnitudeResourceColor = R.color.magnitude1;
                break;
            case 2:
                magnitudeResourceColor = R.color.magnitude2;
                break;
                case 3:
                magnitudeResourceColor = R.color.magnitude3;
                break;
            case 4:
                magnitudeResourceColor = R.color.magnitude4;
                break;
            case 5:
                magnitudeResourceColor = R.color.magnitude5;
                break;
            case 6:
                magnitudeResourceColor = R.color.magnitude6;
                break;
            case 7:
                magnitudeResourceColor = R.color.magnitude7;
                break;
            case 8:
                magnitudeResourceColor = R.color.magnitude8;
                break;
            case 9:
                magnitudeResourceColor = R.color.magnitude9;
                break;
            default:
                magnitudeResourceColor=R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(),magnitudeResourceColor);
    }

    public String formatDate(Date dateObject){

        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL DD, yyyy");
       return  dateFormat.format(dateObject);
    }

    public String formatTime(Date dateObject){

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM,a");
        return timeFormat.format(dateObject);
    }

    public String formatMagnitude(double magnitude){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(magnitude);

    }


}
