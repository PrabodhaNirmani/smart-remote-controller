package com.example.acer.smartremote.Models;

import java.util.UUID;

/**
 * Created by acer on 7/15/2017.
 */

public class RemoteKey {
    private String mRemoteKeyID;
    private String mRemoteKeyName;
    private String mRemoteKeyValue;

    public RemoteKey(String keyName, String keyValue){
        this.mRemoteKeyID = UUID.randomUUID().toString();
        this.mRemoteKeyName = keyName;
        this.mRemoteKeyValue = keyValue;
    }

    public RemoteKey(String remoteKeyID, String remoteKeyName, String keyValue){
        this.mRemoteKeyID = remoteKeyID;
        this.mRemoteKeyName = remoteKeyName;
        this.mRemoteKeyValue = keyValue;
    }

    public String getRemoteKeyID() {
        return mRemoteKeyID;
    }

    public String getRemoteKeyName() {
        return mRemoteKeyName;
    }

    public String getRemoteKeyValues() {
        return mRemoteKeyValue;
    }

    public void setRemoteKeyValue(String data){
        this.mRemoteKeyValue=data;
    }
}
