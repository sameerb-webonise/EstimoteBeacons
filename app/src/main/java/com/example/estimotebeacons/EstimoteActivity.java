package com.example.estimotebeacons;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EstimoteActivity extends AppCompatActivity implements View.OnClickListener {

    //private static final Map<String, List<String>> PLACES_BY_BEACONS;
    private static final String TAG = EstimoteActivity.class.getName();
    private String lastDiscoveredBeacon = "";
    private TextView textView;

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    /*static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        placesByBeacons.put("36015:56457", new ArrayList<String>() {{
            add("Aerobic-ICE");
        }});
        placesByBeacons.put("12830:49469", new ArrayList<String>() {{
            add("Gym-BLUEBERRY");
        }});
        placesByBeacons.put("45892:23678", new ArrayList<String>() {{
            add("Yoga-MINT");
        }});
        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }

    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }

    private BeaconManager beaconManager;
    private BeaconRegion region;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimote);
        textView = (TextView) findViewById(R.id.tvInfo);
        ((Button) findViewById(R.id.btnRefresh)).setOnClickListener(this);
        /*beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {

            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                if (!beacons.isEmpty()) {
                    Beacon nearestBeacon = beacons.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
                    //Log.d(TAG, "Nearest beacon: " + places);
                    if(lastDiscoveredBeacon.equals("") || !lastDiscoveredBeacon.equals(places.get(0))) {
                        Log.d(TAG, "lastDiscoveredBeacon: "+lastDiscoveredBeacon+"\nDiscovered beacon: " + places);
                        String currentTime = Calendar.getInstance().getTime().toString();

                        if(!lastDiscoveredBeacon.equals("")) {
                            String titleExit = "\nExited region: " + lastDiscoveredBeacon;
                            textView.setText(textView.getText() + "\n" + titleExit + "\nAT " + currentTime);
                        }
                        lastDiscoveredBeacon = places.get(0);
                        String titleEntry = "Entered region: "+ lastDiscoveredBeacon;
                        String message = beaconRegion.getProximityUUID()+":"+beaconRegion.getMajor()+":"+beaconRegion.getMinor();
                        showNotification(titleEntry,
                                message);
                        textView.setText(textView.getText()+"\n"+titleEntry+"\nAT "+ currentTime);
                    }
                }

            }
        });

        region = new BeaconRegion("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        refreshBeaconData();
        /*beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });*/
    }

    private void refreshBeaconData() {
        if(textView != null){
            textView.setText(ApplicationController.getInstance().getUpdatedBeaconData());
        }
    }

    @Override
    protected void onPause() {
        //beaconManager.stopRanging(region);

        super.onPause();
    }

    public void showNotification(String title, String message) {
        Context applicationContext = getApplicationContext();
        //Toast.makeText(applicationContext, title+"\n"+message, Toast.LENGTH_SHORT).show();
        Intent notifyIntent = new Intent(applicationContext, EstimoteActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(applicationContext, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(applicationContext)
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRefresh:
                refreshBeaconData();
                break;
        }
    }
}
