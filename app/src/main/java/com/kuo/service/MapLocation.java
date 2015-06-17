package com.kuo.service;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by User on 2015/6/15.
 */
public class MapLocation {

    private static final int gpsMinTime = 500;
    private static final int gpsMinDistance = 0;

    private Context context;
    private LocationManager locationManager;
    private OnLocationListener onLocationListener;

    public MapLocation(Context context){
        this.context = context;
        onGpsCheck();
    }

    public interface OnLocationListener{
        void onLocationChanged(Location location);
        void onStatusChanged(String s, int i, Bundle bundle);
        void onProviderEnabled(String s);
        void onProviderDisabled(String s);
    }

    public void setOnLocationListener(OnLocationListener onLocationListener){
        this.onLocationListener = onLocationListener;
    }

    public void onGpsCheck(){

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String bestProvider = locationManager.getBestProvider(criteria, true);

        if (bestProvider != null && bestProvider.length() > 0)
        {
            locationManager.requestLocationUpdates(bestProvider, gpsMinTime, gpsMinDistance, locationListener);
        }
        else
        {
            List<String> providers = locationManager.getProviders(true);

            for (String provider : providers)
            {
                locationManager.requestLocationUpdates(provider, gpsMinTime, gpsMinDistance, locationListener);
            }
        }

    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            if(onLocationListener != null){
                onLocationListener.onLocationChanged(location);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

            if(onLocationListener != null){
                onLocationListener.onStatusChanged(s, i, bundle);
            }
        }

        @Override
        public void onProviderEnabled(String s) {
            if(onLocationListener != null){
                onLocationListener.onProviderEnabled(s);
            }
        }

        @Override
        public void onProviderDisabled(String s) {
            if(onLocationListener != null){
                onLocationListener.onProviderDisabled(s);
            }
        }
    };

}
