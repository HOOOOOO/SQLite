package com.hochan.sqlite.data;

/**
 * Created by Administrator on 2016/4/12.
 */
public class Worker {
    private String mID;
    private String mName;
    private String mPhoneNumber;
    private String mTowerNumber;
    private String mWorkState;

    public boolean mIsChoosed;

    public Worker() {
    }

    public Worker(String id, String mName, String mPhoneNumber, String mTowerNumber, String mWorkState) {
        this.mID = id;
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mTowerNumber = mTowerNumber;
        this.mWorkState = mWorkState;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmWorkState() {
        return mWorkState;
    }

    public void setmWorkState(String mWorkState) {
        this.mWorkState = mWorkState;
    }

    public String getmTowerNumber() {
        return mTowerNumber;
    }

    public void setmTowerNumber(String mTowerNumber) {
        this.mTowerNumber = mTowerNumber;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }
}
