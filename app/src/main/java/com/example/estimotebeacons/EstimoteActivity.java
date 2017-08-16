package com.example.estimotebeacons;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class EstimoteActivity extends AppCompatActivity implements View.OnClickListener, Notify {

    private static final String TAG = EstimoteActivity.class.getName();
    private TextView textView;
    private RealmResults<BeaconData> beaconDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimote);
        textView = (TextView) findViewById(R.id.tvInfo);
        findViewById(R.id.btnRefresh).setOnClickListener(this);
        ApplicationController.getInstance().setNotify(this);
        refreshBeaconData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
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
                if(beaconDatas != null){
                    textView.setText("");
                    for (BeaconData beaconData:beaconDatas) {
                        textView.append("\nAt "+ApplicationController.getInstance().placesNearBeacon(beaconData.getMajorNo()+":"+ beaconData.getMinorNo())+
                                "\nat "+ beaconData.getTime());
                    }

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
