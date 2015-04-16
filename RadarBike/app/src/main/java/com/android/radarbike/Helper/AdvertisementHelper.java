package com.android.radarbike.Helper;

import android.content.Context;
import android.media.MediaPlayer;

import com.android.radarbike.R;

/**
 * Created by vntalgo on 4/16/2015.
 */
public class AdvertisementHelper {

    public static void triggerAdvertisement(Context context){
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.warning);
        mediaPlayer.start();
    }
}
