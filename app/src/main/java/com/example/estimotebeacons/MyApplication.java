package com.example.estimotebeacons;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.observation.region.Region;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getName();
    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();

        EstimoteSDK.initialize(getApplicationContext(), EstimoteSDK.getAppId() == null ? "App Id" : EstimoteSDK.getAppId(),
                                    EstimoteSDK.getAppToken() == null ? "App Token" : EstimoteSDK.getAppToken());

        beaconManager = new BeaconManager(getApplicationContext());
        ApplicationController.getInstance().initPlaces(getApplicationContext());
        ApplicationController.getInstance().setRangingListener();
        /*beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {

            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List<Beacon> beacons) {
                if(beacons != null && beacons.size() > 0) {
                    for (Beacon beacon : beacons) {
                        String title = "Entered region: "+ beaconRegion.getIdentifier();
                        String message = beacon.getProximityUUID()+":"+beacon.getMajor()+":"+beacon.getMinor();
                        Toast.makeText(MyApplication.this, title+"\n"+message, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, title+"\n"+message);
                        showNotification(title,
                                message);
                    }
                }
            }


            @Override
            public void onExitedRegion(BeaconRegion beaconRegion) {
                //cancelNotification();
                String title = "Exited region: "+ beaconRegion.getIdentifier();
                String message = beaconRegion.getProximityUUID()+":"+beaconRegion.getMajor()+":"+beaconRegion.getMinor();
                Toast.makeText(MyApplication.this, title+"\n"+message, Toast.LENGTH_SHORT).show();
                Log.d(TAG, title+"\n"+message);
                showNotification(title,
                        message);
            }
        });*/


        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion("ice",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 45892, 23678));
            }
        });
        /*beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion("blueberry",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 12830, 49469));
            }
        });*/
    }

    public void cancelNotification() {
        Toast.makeText(this, "cancelNotification", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    public void showNotification(String title, String message) {
        //Toast.makeText(this, title+"\n"+message, Toast.LENGTH_SHORT).show();
        Intent notifyIntent = new Intent(this, EstimoteActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}
