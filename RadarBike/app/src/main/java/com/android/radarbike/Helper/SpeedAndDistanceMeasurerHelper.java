package com.android.radarbike.Helper;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.android.radarbike.utils.Logger;

/**
 * Created by vntalgo on 3/31/2015.
 */
public class SpeedAndDistanceMeasurerHelper {

    private static LocationManager locationManager;
    private static LocationListener locationListener;
    private static Location currentLocation;
    private static Object lock = new Object();

    private static final float CYCLIST_SPEED_MIN_THRESHOLD = 2.77777778f;
    private static final float CYCLIST_SPEED_MAX_THRESHOLD = 9.72222223f;
    private static final float CYCLIST_DISTANCE_THRESHOLD = 50f;

    private static void initLocationListener(Context context){
        // Acquire a reference to the system
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                synchronized (lock) {
                    currentLocation = location;
                    Logger.LOGD("Updated speed is:" + currentLocation.getSpeed());
                    Logger.LOGD("Updated location is: "+ currentLocation);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        final String provider = locationManager.getBestProvider(criteria, false);
        Logger.LOGD("best location provider is: "+ provider);
        currentLocation = locationManager.getLastKnownLocation(provider);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                locationManager
                        .requestLocationUpdates(provider, 0, 0, locationListener);
            }
        });
    }

    public static Location getLastLocation(Context context){
        if(locationListener == null){
            initLocationListener(context);
        }

        synchronized (lock) {
            Logger.LOGD("current location is: "+ currentLocation);
            return currentLocation;
        }
    }

    public static boolean isCyclistThresholdReached(Context context){
        boolean result = false;

        Location location = getLastLocation(context);

        if(location != null) {
            if (location.getSpeed() >= CYCLIST_SPEED_MIN_THRESHOLD
                    && location.getSpeed() <= CYCLIST_SPEED_MAX_THRESHOLD) {
                Logger.LOGD("Current speed is: " + location.getSpeed());
                result = true;
            }
        }

        Logger.LOGD("isCyclistThresholdReached: "+ result);

        return result;
    }

    public static boolean alertDriver(Context context, double lat, double lng){
        boolean result = false;
        float results[] = new float[3];

        Location location = getLastLocation(context);

        if(location != null) {
            Location.distanceBetween(lat, lng, location.getLatitude(), location.getLongitude(), results);

            if (results[0] <= CYCLIST_DISTANCE_THRESHOLD) {
                result = true;
            }
        }

        Logger.LOGD("alertDriver: "+ result);

        return result;
    }
}