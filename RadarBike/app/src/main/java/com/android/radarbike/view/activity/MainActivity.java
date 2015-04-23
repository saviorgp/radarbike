package com.android.radarbike.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.android.radarbike.Helper.AdvertisementHelper;
import com.android.radarbike.Helper.NotificationHelper;
import com.android.radarbike.R;
import com.android.radarbike.service.RadarBikeService;
import com.android.radarbike.utils.Constants;
import com.android.radarbike.utils.Logger;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btDriver).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    findViewById(R.id.main_layout).setBackground(getResources().getDrawable(R.drawable.driver));
            }
        });

        findViewById(R.id.btCyclist).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(R.id.main_layout).setBackground(getResources().getDrawable(R.drawable.cyclist));
            }
        });

        /* check if TTS is installed */
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        this.startActivityForResult(checkTTSIntent, Constants.TTS_AVAILABILITY_CHECK_CODE);
        //AdvertisementHelper.triggerTTSAdvertisement(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_driver_settings) {
            RadarBikeService.startActionDriver(this);
            NotificationHelper.showNotification(getApplicationContext(), Constants.APPMODE.Car);
        } else if (id == R.id.action_cyclist_settings){
            RadarBikeService.startActionCyclist(this);
            NotificationHelper.showNotification(getApplicationContext(), Constants.APPMODE.Cyclist);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.TTS_AVAILABILITY_CHECK_CODE) {
            Logger.LOGD("TTS request code is: " + resultCode);
            if (resultCode != TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
}