package com.example.chapter_19;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import java.util.Random;

public class OdometerService extends Service {
    private final IBinder binder = new OdometerBinder();
    LocationListener listener;
    Location lastLocation;
    double distance;
    LocationManager locationManager;

    public OdometerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (lastLocation != null) {
                    distance = location.distanceTo(lastLocation);
                    lastLocation = location;
                } else {
                    lastLocation = location;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
         locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
         String provider = locationManager.getBestProvider(new Criteria(), true);
            if(provider != null){
                locationManager.requestLocationUpdates(provider,1000,1, listener);

            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager !=null && listener != null){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                locationManager.removeUpdates(listener);
            }
        }
        locationManager = null;
        listener = null;
    }

    public class OdometerBinder extends Binder {
        OdometerService getOdometerService() {
            return OdometerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    public double getDistance() {
        return distance;
    }

}
