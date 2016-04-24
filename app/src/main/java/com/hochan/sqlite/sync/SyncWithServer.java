package com.hochan.sqlite.sync;

import com.hochan.sqlite.data.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 16-4-22.
 */
public class SyncWithServer {
    private static SyncWithServer sSyncWithServer;
    private Map<String,Worker> mModifyWorkers;
    private List<Integer> mDeleteWorkers;

    private SyncWithServer() {
        mModifyWorkers = new HashMap<String, Worker>();
        mDeleteWorkers = new ArrayList<Integer>();
    }

    public static SyncWithServer getInstance() {
        if (sSyncWithServer == null) {
            sSyncWithServer = new SyncWithServer();
        }
        return sSyncWithServer;
    }

    public Map<String, Worker> getmModifyWorkers() {
        return mModifyWorkers;
    }

    public List<Integer> getDeleteWorkers() {
        return mDeleteWorkers;
    }

    public void ClearModifyWorkers(){
        mModifyWorkers.clear();
    }

    public void ClearDeleteWorkers() {
        mDeleteWorkers.clear();
    }

    public void ShowModifyWorkers(){
        System.out.println("sizeof mm is " + mModifyWorkers.size());
        for (Map.Entry<String, Worker> entry : mModifyWorkers.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    " name = " + entry.getValue().getmName() +
                    " phone = " + entry.getValue().getmPhoneNumber() +
                    " tower = " + entry.getValue().getmTowerNumber() +
                    " state = " + entry.getValue().getmWorkState());
        }
    }

    public void ShowDeleteWorkers() {
        System.out.print("delete id: ");
        for (int i= 0; i<mDeleteWorkers.size(); i++ ) {
            System.out.print(mDeleteWorkers.get(i) + " ");
        }
        System.out.println();
    }
}
