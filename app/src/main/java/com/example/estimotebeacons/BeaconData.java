package com.example.estimotebeacons;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by webonise on 16/8/17.
 */

public class BeaconData extends RealmObject {
    String uuid;
    int majorNo;
    int minorNo;
    @PrimaryKey
    int primaryKey;
    String macAddr;
    int measuredPower;
    int rssi;
    String uniqueKey;
    String time;
    long timeInMillis;

    public BeaconData(){

    }

    public BeaconData(String uuid, int majorNo, int minorNo, String macAddr, int measuredPower, int rssi, String uniqueKey, String time, long timeInMillis) {
        this.uuid = uuid;
        this.majorNo = majorNo;
        this.minorNo = minorNo;
        this.primaryKey = majorNo+minorNo;
        this.macAddr = macAddr;
        this.measuredPower = measuredPower;
        this.rssi = rssi;
        this.uniqueKey = uniqueKey;
        this.time = time;
        this.timeInMillis = timeInMillis;
    }

    public String getUuid() {
        return uuid;
    }

    public int getMajorNo() {
        return majorNo;
    }

    public int getMinorNo() {
        return minorNo;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public int getMeasuredPower() {
        return measuredPower;
    }

    public int getRssi() {
        return rssi;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public String getTime(){
        return time;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setMajorNo(int majorNo) {
        this.majorNo = majorNo;
    }

    public void setMinorNo(int minorNo) {
        this.minorNo = minorNo;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public void setMeasuredPower(int measuredPower) {
        this.measuredPower = measuredPower;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
