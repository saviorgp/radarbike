package com.android.radarbike.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.radarbike.helper.AdvertisementHelper;
import com.android.radarbike.helper.NotificationHelper;
import com.android.radarbike.helper.SpeedAndDistanceMeasurerHelper;
import com.android.radarbike.helper.WebServiceHelper;
import com.android.radarbike.model.PositionsVO;
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
    private static final String ACTION_NO_MODE = "com.android.radarbike.service.action.NO_MODE";
    private static volatile String currentMode = ACTION_NO_MODE;
    private static Activity currentActivity;

    private static final int SERVICE_REQUEST_FREQUENCY = 15000;


    public RadarBikeService() {
        super("RadarBikeService");
    }

    /**
     * Starts this service to perform driver action. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDriver(Context context) {
        if(context instanceof Activity){
            currentActivity = (Activity) context;
        }

        /* only starts the server if a different mode was selected */
        if(!currentMode.equals(ACTION_DRIVER)) {
            currentMode = ACTION_DRIVER;
            triggerActionDriver(context);
            SpeedAndDistanceMeasurerHelper.updatePositionCheckout(context);
        }
    }

    private static void triggerActionDriver(Context context){
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
        if (context instanceof Activity) {
            currentActivity = (Activity) context;
        }

         /* only starts the server if a different mode was selected */
        if(!currentMode.equals(ACTION_CYCLIST)) {
            currentMode = ACTION_CYCLIST;
            triggerActionCyclist(context);
        }
    }


    private static void triggerActionCyclist(Context context){
        Intent intent = new Intent(context, RadarBikeService.class);
        intent.setAction(ACTION_CYCLIST);
        context.startService(intent);
    }

    public static void stopService(){
        currentMode = ACTION_NO_MODE;
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
                List<PositionsVO> posList =
                       WebServiceHelper.getPositions(RadarBikeService.this.getApplicationContext());
                Logger.LOGD("pos list size:" + posList.size());
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
                if(currentMode.equals(ACTION_DRIVER)) {
                    // TODO change to trigger audio alerts
                    Logger.LOGD("Modo motorista em execução!");
                    if (o.intValue() == 1) {
                        Toast.makeText(RadarBikeService.this.getApplicationContext(),
                                "Ciclista por perto!!", Toast.LENGTH_LONG).show();
                        AdvertisementHelper
                                .triggerAdvertisement(RadarBikeService.this.getApplicationContext());
                        AdvertisementHelper.triggerTTSAdvertisement(currentActivity);
                    } else if (o.intValue() > 1) {
                        Toast.makeText(RadarBikeService.this.getApplicationContext(),
                                "Ciclistas por perto!!", Toast.LENGTH_LONG).show();
                        AdvertisementHelper
                                .triggerAdvertisement(RadarBikeService.this.getApplicationContext());
                        AdvertisementHelper.triggerTTSAdvertisement(currentActivity);
                    }


                    /** makes a new server request after an specific elapsed time */
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            RadarBikeService
                                    .triggerActionDriver(RadarBikeService.this.getApplicationContext());
                        }
                    }, SERVICE_REQUEST_FREQUENCY);
                }
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
                Logger.LOGD("Modo ciclista em execução!");
                if(SpeedAndDistanceMeasurerHelper
                        .isCyclistThresholdReached(RadarBikeService.this.getApplicationContext())){
                    Location location =
                            SpeedAndDistanceMeasurerHelper
                                    .getLastLocation(RadarBikeService.this.getApplicationContext());
                    if(location != null) {
                        WebServiceHelper.sendPosition(RadarBikeService.this.getApplicationContext(),
                                new PositionsVO(location.getLatitude(),
                                        location.getLongitude()));
                    }
                }

                if(currentMode.equals(ACTION_CYCLIST)) {
                    /** makes a new server request after an specific elapsed time */
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            RadarBikeService
                               .triggerActionCyclist(RadarBikeService.this.getApplicationContext());
                        }
                    }, SERVICE_REQUEST_FREQUENCY);
                }

                return null;
            }
        }.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(currentMode.equals(ACTION_NO_MODE)) {
            SpeedAndDistanceMeasurerHelper.updatePositionCheckout(getApplicationContext());
            NotificationHelper.dismissNotification(getApplicationContext());
        }
    }
}