package com.example.earthquake_report;

public class EarthQuake {

    double mMagnitude;
    String mLocation;
    String mDate;
    Long mTimeInMilliSeconds;
    String mUrl;

    public EarthQuake(double magnitude,String location,long time,String url){
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliSeconds = time;
        mUrl = url;
    }

    public double getMagnitude(){
        return mMagnitude;
    }

    public String getLocation(){
        return mLocation;
    }

    public String getDate(){
        return mDate;
    }

    public Long getTime(){
        return mTimeInMilliSeconds;
    }

    public String getUrl(){
        return mUrl;
    }
}

