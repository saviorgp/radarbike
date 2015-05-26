package com.android.radarbike.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.radarbike.utils.Constants;
import com.android.radarbike.view.activity.MainActivity;

/**
 * Created by AlexGP on 30/04/2015.
 */
public class GPSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {

            Intent startIntent = new Intent(context, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.putExtra(Constants.GPS_LAUNCHED, true);
            context.startActivity(startIntent);
        }
    }
}
