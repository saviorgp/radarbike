package com.android.radarbike.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.radarbike.Helper.AdvertisementHelper;
import com.android.radarbike.Helper.SpeedAndDistanceMeasurerHelper;
import com.android.radarbike.Helper.WebServiceHelper;
import com.android.radarbike.model.PositionsVO;
import com.android.radarbike.model.component.WebServiceWrapper;
import com.android.radarbike.utils.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RadarBikeService extends IntentService {

    private static final String ACTION_DRIVER = "com.android.radarbike.service.action.DRIVER";
    private static final String ACTION_CYCLIST = "com.android.radarbike.service.action.CYCLIST";

    private static final int SERVICE_REQUEST_FREQUENCY = 20000;

    /**
     * Starts this service to perform driver action. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDriver(Context context) {
        Intent intent = new Intent(context, RadarBikeService.class);
        intent.setAction(ACTION_DRIVER);
        context.startService(intent);
    }

    /**
     * Starts this service to perform cyclist action. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionCyclist(Context context) {
        Intent intent = new Intent(context, RadarBikeService.class);
        intent.setAction(ACTION_CYCLIST);
        context.startService(intent);
    }

    public RadarBikeService() {
        super("RadarBikeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.LOGD("Radar Bike service running");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DRIVER.equals(action)) {
                handleActionDriver();
            } else if (ACTION_CYCLIST.equals(action)) {
                handleActionCyclist();
            }
        }
    }

    /**
     * Handle driver action in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDriver() {
        new AsyncTask<Object, Void, Integer>() {

            @Override
            protected Integer doInBackground(Object... params) {
                List<PositionsVO> posList = WebServiceHelper.getPositions();
                int counter = 0;
                /* check if there is any cyclist nearby. If one is found then the alert is trigged */
                for(PositionsVO vo : posList){
                    if(SpeedAndDistanceMeasurerHelper
                            .alertDriver(RadarBikeService.this.getApplicationContext(),
                                         vo.getLat(),vo.getLng())){
                        ++counter;
                    }
                }

                return new Integer(counter);
            }

            @Override
            protected void onPostExecute(Integer o) {
               // TODO change to trigger audio alerts
               if(o.intValue() == 1){
                   Toast.makeText(RadarBikeService.this.getApplicationContext(),
                                  "Ciclista por perto!!", Toast.LENGTH_LONG).show();
                   AdvertisementHelper
                           .triggerAdvertisement(RadarBikeService.this.getApplicationContext());
               } else if(o.intValue() > 1){
                   Toast.makeText(RadarBikeService.this.getApplicationContext(),
                                  "Ciclistas por perto!!", Toast.LENGTH_LONG).show();
                   AdvertisementHelper
                           .triggerAdvertisement(RadarBikeService.this.getApplicationContext());
               }

               /** makes a new server request after an specific elapsed time */
               Timer t = new Timer();
               t.schedule(new TimerTask() {
                   @Override
                   public void run() {
                       RadarBikeService
                               .startActionDriver(RadarBikeService.this.getApplicationContext());
                   }
               },SERVICE_REQUEST_FREQUENCY);
            }
        }.execute();
    }

    /**
     * Handle cyclist action in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCyclist() {
        new AsyncTask<Object, Void, Integer>() {

            @Override
            protected Integer doInBackground(Object... params) {
                if(SpeedAndDistanceMeasurerHelper
                        .isCyclistThresholdReached(RadarBikeService.this.getApplicationContext())){
                    Location location =
                            SpeedAndDistanceMeasurerHelper
                                    .getLastLocation(RadarBikeService.this.getApplicationContext());
                    WebServiceHelper.sendPosition(RadarBikeService.this.getApplicationContext(),
                                                  new PositionsVO(location.getLatitude(),
                                                                  location.getLongitude()));
                }

                /** makes a new server request after an specific elapsed time */
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        RadarBikeService
                                .startActionCyclist(RadarBikeService.this.getApplicationContext());
                    }
                },SERVICE_REQUEST_FREQUENCY);

                return null;
            }
        }.execute();
    }
}