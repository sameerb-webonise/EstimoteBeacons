package com.example.estimotebeacons;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class EstimoteActivity extends AppCompatActivity implements View.OnClickListener, Notify {

    private static final String TAG = EstimoteActivity.class.getName();
    private static final int PERMISSION_REQUEST_LOCATION = 100;
    private static final int REQUEST_ENABLE_BLUETOOTH = 200;
    private TextView textView;
    private RealmResults<BeaconData> beaconDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimote);
        textView = (TextView) findViewById(R.id.tvInfo);
        findViewById(R.id.btnRefresh).setOnClickListener(this);
        ApplicationController.getInstance().setNotify(this);
        //refreshBeaconData();
        startMonitoring();
    }

    private void startMonitoring(){
        if(isBluetoothEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int result = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    ApplicationController.getInstance().setRangingListener();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
            } else {
                ApplicationController.getInstance().setRangingListener();
            }
        } else {
            startBluetoothIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMonitoring();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        permissions[0])) {
                    Log.d( TAG , "This feature will not work as permission is denied");
                    Toast.makeText(this, getString(R.string.msg_permission_denied), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d( TAG , "Permission denied forever");
                    Snackbar snackBar = Snackbar.make(findViewById(R.id.root),  getString(R.string.enable_permission),
                            Snackbar.LENGTH_LONG);
                    snackBar.setAction(R.string.action_enable, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            launchApplicationSetting();
                        }
                    });
                    snackBar.show();
                }
            }
        }
    }

    public void launchApplicationSetting() {
        Intent appIntent = new Intent();
        appIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        appIntent.addCategory(Intent.CATEGORY_DEFAULT);
        appIntent.setData(Uri.parse("package:" + getPackageName()));
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(appIntent);
    }

    public boolean isBluetoothEnabled() {
        BluetoothManager bluetoothManager = (BluetoothManager) MyApplication.getAppContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            return true;
        }
        return false;
    }

    public void startBluetoothIntent() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_ENABLE_BLUETOOTH:
                    if (resultCode == Activity.RESULT_CANCELED) {
                            Snackbar snackBar = Snackbar.make(findViewById(R.id.root),  getString(R.string.enable_bluetooth_settings),
                                Snackbar.LENGTH_LONG);
                            snackBar.setAction(R.string.action_settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent();
                                    i.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                                    startActivity(i);
                                }
                            });
                            snackBar.show();
                    } else if (resultCode == Activity.RESULT_OK) {
                        startMonitoring();
                    }
                    break;
            }
        } catch (Exception ex) {
            Log.d(TAG, "OnActivityResult: Exception: " + ex.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //SystemRequirementsChecker.checkWithDefaultDialogs(this);
        refreshBeaconData();
    }

    private void refreshBeaconData() {
        /*if(textView != null){
            textView.setText(ApplicationController.getInstance().getUpdatedBeaconData());
        }*/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                beaconDatas = realm.where(BeaconData.class).findAllSorted("timeInMillis", Sort.DESCENDING);
                if(beaconDatas != null && beaconDatas.size()>0){
                    //textView.setText("");
                    /*for (BeaconData beaconData:beaconDatas) {
                        textView.append("\nAt "+ApplicationController.getInstance().placesNearBeacon(beaconData.getMajorNo()+":"+ beaconData.getMinorNo())+
                                "\nat "+ beaconData.getTime());
                    }*/
                    BeaconData beaconData = beaconDatas.get(0);
                    textView.append("\nAt "+ApplicationController.getInstance().placesNearBeacon(beaconData.getMajorNo()+":"+ beaconData.getMinorNo())+
                            "\nat "+ beaconData.getTime());

                    //textView.setText(ApplicationController.getInstance().getUpdatedBeaconData());

                }
                realm.close();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRefresh:
                refreshBeaconData();
                break;
        }
    }

    @Override
    public void updateData() {
        refreshBeaconData();
    }
}
