package com.android.radarbike.Helper;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by vntalgo on 3/31/2015.
 */
public class SpeedMeasurerHelper {

    private static LocationManager locationManager;

    private static final float CYCLIST_SPEED_MIN_THRESHOLD = 2.77777778f;
    private static final float CYCLIST_SPEED_MAX_THRESHOLD = 9.72222223f;

    public static boolean isCyclistThresholdReached(Context context){
        boolean result = false;

        // Get the location manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use default
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if(location.getSpeed() >= CYCLIST_SPEED_MIN_THRESHOLD
                && location.getSpeed() <= CYCLIST_SPEED_MAX_THRESHOLD){
            result = true;
        }

        return result;
    }
}