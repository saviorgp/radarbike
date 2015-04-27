package com.android.radarbike.Helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.android.radarbike.R;
import com.android.radarbike.utils.Constants;
import com.android.radarbike.view.activity.MainActivity;

/**
 * Created by AlexGP on 17/04/2015.
 */
public class NotificationHelper {

    private static int NOTIFICATION_ID = 1;

    public static void showNotification(Context context, Constants.APPMODE mode){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        int icon = 0;
        CharSequence tickerText = "RadarBike";
        CharSequence contentTitle = "RadarBike em execução.";
        CharSequence contentText;

        // TODO change icon accordingly

        switch (mode){
            case Car:
                contentText = "Modo carro ativado";
                icon = R.drawable.car;
                break;
            case Bus:
                contentText = "Modo ônibus ativado";
                icon = R.drawable.car;
                break;
            case Bike:
                contentText = "Modo moto ativado";
                icon = R.drawable.car;
                break;
            case Taxi:
                contentText = "Modo taxi ativado";
                icon = R.drawable.car;
                break;
            case Truck:
                contentText = "Modo caminhão ativado";
                icon = R.drawable.car;
                break;
            case Cyclist:
                contentText = "Modo ciclista ativado";
                icon = R.drawable.bike;
                break;
            default:
                contentText = "";
                break;
        }

        Notification.Builder builder = null;
        NotificationCompat.Builder supportBuilder = null;
        Notification notification = null;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent, 0);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            supportBuilder = new NotificationCompat.Builder(context);

            supportBuilder.setContentIntent(contentIntent)
                    .setSmallIcon(icon)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText);

            notification = supportBuilder.build();
        } else {
           builder = new Notification.Builder(context);

           builder.setContentIntent(contentIntent)
                    .setSmallIcon(icon)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText);

           notification = builder.build();
        }

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static void dismissNotification(Context context){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}