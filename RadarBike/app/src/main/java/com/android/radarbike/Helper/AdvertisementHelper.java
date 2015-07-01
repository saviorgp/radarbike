package com.android.radarbike.helper;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.android.radarbike.R;

/**
 * Created by vntalgo on 4/16/2015.
 */
public class AdvertisementHelper {

    private static TextToSpeech ttsInstance;

    private static TextToSpeech.OnInitListener listener;

    public static void triggerAdvertisement(Context context){
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.warning);
        mediaPlayer.start();
    }

    public static void triggerTTSAdvertisement(Activity context){
        if(ttsInstance == null){
            initListener(context);
            ttsInstance = new TextToSpeech(context, listener);
        } else {
           playTTSAdvertisement(context);
        }
    }

    private static void initListener(final Activity context){
        listener = new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.ERROR) {
                    Toast.makeText(context,"Error ao inicializar TTS...", Toast.LENGTH_LONG).show();
                    ttsInstance = null;
                } else {
                    playTTSAdvertisement(context);
                }
            }
        };
    }

    private static void playTTSAdvertisement(Activity context){
        ttsInstance.setLanguage(context.getResources().getConfiguration().locale);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsInstance.speak(context.getString(R.string.driver_advise),
                    TextToSpeech.QUEUE_FLUSH, null, "REQUEST_TTS");
        } else {
            ttsInstance.speak(context.getString(R.string.driver_advise),
                    TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}