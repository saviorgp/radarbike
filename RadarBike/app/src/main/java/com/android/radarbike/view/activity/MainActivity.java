package com.android.radarbike.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.radarbike.Helper.AdvertisementHelper;
import com.android.radarbike.Helper.NotificationHelper;
import com.android.radarbike.R;
import com.android.radarbike.model.Preferences;
import com.android.radarbike.service.RadarBikeService;
import com.android.radarbike.utils.Constants;
import com.android.radarbike.utils.Logger;

import at.markushi.ui.CircleButton;


public class MainActivity extends ActionBarActivity {

    private CircleButton btDriver = null;
    private CircleButton btCyclist = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();

        btDriver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                btDriver.setColor(getResources().getColor(R.color.bt_active));
                btCyclist.setColor(getResources().getColor(R.color.bt_no_active));
                btDriver.invalidate();

                findViewById(R.id.main_layout).setBackground(getResources().getDrawable(R.drawable.driver));

                showDialogDriverMode();
            }
        });

        btCyclist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                btDriver.setColor(getResources().getColor(R.color.bt_no_active));
                btCyclist.setColor(getResources().getColor(R.color.bt_active));

                findViewById(R.id.main_layout).setBackground(getResources().getDrawable(R.drawable.cyclist));

                RadarBikeService.startActionCyclist(MainActivity.this);
                NotificationHelper.showNotification(getApplicationContext(), Constants.APPMODE.Cyclist);
                Preferences.getPreferences(getApplicationContext())
                        .editPreference(Constants.SELECTED_MODE, R.id.btCyclist);

                // TODO impl. info dialog
                toBackground();
            }
        });

        /* check if TTS is installed */
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        this.startActivityForResult(checkTTSIntent, Constants.TTS_AVAILABILITY_CHECK_CODE);
        //AdvertisementHelper.triggerTTSAdvertisement(this);
        //AdvertisementHelper.triggerAdvertisement(getApplicationContext());
    }

    /**
     * Init View components
     */
    private void initComponent(){

        btDriver = (CircleButton)findViewById(R.id.btDriver);
        btCyclist = (CircleButton)findViewById(R.id.btCyclist);
    }

    private void showDialogDriverMode(){

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.driver_mode_choice);
        dialog.setTitle(getString(R.string.driver_mode));
        dialog.setCancelable(false);

        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);

        dialog.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId != -1){
                    RadarBikeService.startActionDriver(MainActivity.this);
                    if (selectedId == R.id.rb_motorcycler){
                        Toast.makeText(getApplicationContext(),"moto", Toast.LENGTH_LONG).show();
                        NotificationHelper.showNotification(getApplicationContext(),
                                                            Constants.APPMODE.Bike);
                        Preferences.getPreferences(getApplicationContext())
                                .editPreference(Constants.SELECTED_MODE,
                                                R.id.rb_motorcycler);
                    } else if (selectedId == R.id.rb_car){
                        Toast.makeText(getApplicationContext(),"car", Toast.LENGTH_LONG).show();
                        NotificationHelper.showNotification(getApplicationContext(),
                                                            Constants.APPMODE.Car);
                        Preferences.getPreferences(getApplicationContext())
                                .editPreference(Constants.SELECTED_MODE,
                                                R.id.rb_car);

                    } else if (selectedId == R.id.rb_taxi){
                        Toast.makeText(getApplicationContext(),"taxi", Toast.LENGTH_LONG).show();
                        NotificationHelper.showNotification(getApplicationContext(),
                                                            Constants.APPMODE.Taxi);
                        Preferences.getPreferences(getApplicationContext())
                                .editPreference(Constants.SELECTED_MODE,
                                                R.id.rb_taxi);

                    } else if (selectedId == R.id.rb_truck){
                        Toast.makeText(getApplicationContext(),"truck", Toast.LENGTH_LONG).show();
                        NotificationHelper.showNotification(getApplicationContext(),
                                                            Constants.APPMODE.Truck);
                        Preferences.getPreferences(getApplicationContext())
                                .editPreference(Constants.SELECTED_MODE,
                                                R.id.rb_truck);
                    }
                }
                dialog.dismiss();

                // TODO impl. info dialog
                toBackground();
            }
        });

        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

                btDriver.setColor(getResources().getColor(R.color.bt_no_active));
                btCyclist.setColor(getResources().getColor(R.color.bt_no_active));

                findViewById(R.id.main_layout).setBackground(getResources().getDrawable(R.drawable.init));
            }
        });

        int selectedMode =
                Preferences.getPreferences(getApplicationContext())
                        .getSelectedModePreference();
        if(selectedMode != 0 && selectedMode != R.id.btCyclist){
            radioGroup.check(selectedMode);
        } else{
            radioGroup.check(R.id.rb_motorcycler);
        }

        dialog.show();
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
            //RadarBikeService.startActionDriver(this);
            //NotificationHelper.showNotification(getApplicationContext(), Constants.APPMODE.Car);
        } else if (id == R.id.action_cyclist_settings){
            //RadarBikeService.startActionCyclist(this);
            //NotificationHelper.showNotification(getApplicationContext(), Constants.APPMODE.Cyclist);
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

    private void toBackground(){
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }
}