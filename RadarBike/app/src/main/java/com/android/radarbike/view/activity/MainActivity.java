package com.android.radarbike.view.activity;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.radarbike.Helper.SpeedAndDistanceMeasurerHelper;
import com.android.radarbike.R;
import com.android.radarbike.service.RadarBikeService;
import com.android.radarbike.utils.Logger;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Acquire a reference to the system
        LocationManager locationManager = (LocationManager) this .getSystemService(LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Logger.LOGD("Current speed:" + location.getSpeed());
                Toast.makeText(MainActivity.this, "Current speed:" + location.getSpeed(),
                        Toast.LENGTH_LONG).show();

            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            public void onProviderEnabled(String provider) {
            }
            public void onProviderDisabled(String provider) {
            } };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


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
        } else if (id == R.id.action_cyclist_settings){
            RadarBikeService.startActionCyclist(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
