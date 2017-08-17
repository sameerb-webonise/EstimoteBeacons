package com.example.estimotebeacons;

import android.app.Application;
import android.content.Context;

import com.estimote.coresdk.common.config.EstimoteSDK;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getName();
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(getString(R.string.app_name) /*Realm.DEFAULT_REALM_NAME*/)
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        EstimoteSDK.initialize(getApplicationContext(), EstimoteSDK.getAppId() == null ? "App Id" : EstimoteSDK.getAppId(),
                                    EstimoteSDK.getAppToken() == null ? "App Token" : EstimoteSDK.getAppToken());

        ApplicationController.getInstance().initPlaces(getApplicationContext());
        /*beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion("ice",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 45892, 23678));
            }
        });*/
    }

    public static Context getAppContext(){
        return applicationContext;
    }
}
