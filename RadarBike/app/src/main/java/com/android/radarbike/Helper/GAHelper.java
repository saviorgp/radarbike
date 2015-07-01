package com.android.radarbike.helper;

import android.content.Context;

import com.android.radarbike.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by AlexGP on 09/05/2015.
 */
public class GAHelper {

    private static Tracker tracker;

    private static Tracker getTracker(Context context){
        if(tracker == null){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            tracker = analytics.newTracker(R.xml.app_tracker);
        }

        return tracker;
    }

    public static void sendGoogleAnalyticsScreenData(String screenName, Context context){
        Tracker t = getTracker(context);
        t.setScreenName(screenName);
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static void sendGoogleAnalyticsEventData(String category, String action, String label, Context context){
        Tracker t = getTracker(context);
        t.send(new HitBuilders.EventBuilder().setCategory(category)
                .setAction(action)
                .setLabel(label).build());
    }

    public static void sendGoogleAnalyticsEventData(String category, String action, String label, long value, Context context){
        Tracker t = getTracker(context);
        t.send(new HitBuilders.EventBuilder().setCategory(category)
                .setAction(action)
                .setLabel(label).setValue(value)
                .build());
    }
}
