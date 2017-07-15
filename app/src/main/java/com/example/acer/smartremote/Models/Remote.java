package com.example.acer.smartremote.Models;

import android.widget.Switch;

import com.example.acer.smartremote.activities.MainActivity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by acer on 7/15/2017.
 */

public class Remote {

    private String mRemoteID;
    private String mRemoteBrand;
    private ArrayList<RemoteKey> mRemoteKeys;

    public Remote(String remoteBrand, ArrayList<RemoteKey> remoteKeys){
        this.mRemoteID = UUID.randomUUID().toString();
        this.mRemoteBrand = remoteBrand;
        this.mRemoteKeys = remoteKeys;

    }

    public Remote(String remoteID, String remoteBrand, ArrayList<RemoteKey> remoteKeys){
        this.mRemoteID = remoteID;
        this.mRemoteBrand = remoteBrand;
        this.mRemoteKeys = remoteKeys;
    }


    public String getRemoteID() {
        return mRemoteID;
    }

    public String getRemoteBrand() {
        return mRemoteBrand;
    }

    public ArrayList<RemoteKey> getRemoteKeys() {
        return this.mRemoteKeys;
    }

}
