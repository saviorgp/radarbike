package com.android.radarbike.Helper;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by vntalgo on 3/31/2015.
 */
public class SpeedAndDistanceMeasurerHelper {

    private static LocationManager locationManager;

    private static final float CYCLIST_SPEED_MIN_THRESHOLD = 2.77777778f;
    private static final float CYCLIST_SPEED_MAX_THRESHOLD = 9.72222223f;
    private static final float CYCLIST_DISTANCE_THRESHOLD = 50f;

    public static Location getLastLocation(Context context){

        // Get the location manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use default
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        return location;
    }

    public static boolean isCyclistThresholdReached(Context context){
        boolean result = false;

        Location location = getLastLocation(context);

        if(location.getSpeed() >= CYCLIST_SPEED_MIN_THRESHOLD
                && location.getSpeed() <= CYCLIST_SPEED_MAX_THRESHOLD){
            result = true;
        }

        return result;
    }

    public static boolean alertDriver(Context context, double lat, double lng){
        boolean result = false;
        float results[] = new float[3];

        Location location = getLastLocation(context);

        Location.distanceBetween(lat,lng,location.getLatitude(),location.getLongitude(),results);

        if(results[0] <= CYCLIST_DISTANCE_THRESHOLD){
            result = true;
        }

        return result;
    }
}