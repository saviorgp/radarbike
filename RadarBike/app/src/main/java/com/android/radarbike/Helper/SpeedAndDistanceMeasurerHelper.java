package com.android.radarbike.Helper;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.android.radarbike.utils.Logger;

import java.util.List;

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

        List<String> lProviders = locationManager.getProviders(false);
        for(int i=0; i<lProviders.size(); i++){
            Logger.LOGD(lProviders.get(i));
        }

        String provider = locationManager.getBestProvider(criteria, false);
        Logger.LOGD("best location provider is: "+ provider);
        Location location = locationManager.getLastKnownLocation(provider);

        return location;
    }

    public static boolean isCyclistThresholdReached(Context context){
        boolean result = false;

        Location location = getLastLocation(context);

        if(location.getSpeed() >= CYCLIST_SPEED_MIN_THRESHOLD
                && location.getSpeed() <= CYCLIST_SPEED_MAX_THRESHOLD){
            Logger.LOGD("Current speed is: " + location.getSpeed());
            result = true;
        }

        Logger.LOGD("isCyclistThresholdReached: "+ result);

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

        Logger.LOGD("alertDriver: "+ result);

        return result;
    }
}