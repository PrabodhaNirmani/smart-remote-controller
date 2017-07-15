package com.example.acer.smartremote.activities;

import android.animation.IntArrayEvaluator;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.acer.smartremote.BluetoothThread;
import com.example.acer.smartremote.Models.Remote;
import com.example.acer.smartremote.Models.RemoteKey;
import com.example.acer.smartremote.Models.RemoteLab;
import com.example.acer.smartremote.R;

import java.util.ArrayList;

public class RemoteActivity extends AppCompatActivity {


    // Tag for logging
    private static final String TAG = "RemoteActivity";
    private final int REQUEST_ENABLE_BT = 1;

    //bundle argument name for remote acitivity intent
    private static final String EXTRA_REMOTE_ID = "com.example.acer.smartremote.remoteid";

    // MAC address of remote Bluetooth device
    // Replace this with the address of your own module
    //00:21:13:00:B4:E9
    private final String address = "00:21:13:00:B3:CB";

    // The thread that does all the work
    BluetoothThread btt;

    // Handler for writing messages to the Bluetooth connection
    Handler writeHandler;

    private ImageButton powerButton;
    private ImageButton channelUpButton;
    private ImageButton channelDownButton;
    private ImageButton volumeUpButton;
    private ImageButton volumeDownButton;


    private ArrayList<Remote> mRemotes;
    private Remote mRemote;

    public static Intent newIntent(Context packageContext, String remoteID){
        Intent intent = new Intent(packageContext, RemoteActivity.class);
        intent.putExtra(EXTRA_REMOTE_ID, remoteID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        String remoteID = (String) getIntent()
                .getSerializableExtra(EXTRA_REMOTE_ID);
        mRemotes = RemoteLab.get(getApplicationContext()).getRemotes();

        for (Remote remote: mRemotes){
            if (remote.getRemoteID().equals(remoteID)){
                this.mRemote = remote;
            }
        }

        powerButton = (ImageButton) findViewById(R.id.btn_on_off);
        powerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String data = "";
                ArrayList<RemoteKey> mRemoteKeys = mRemote.getRemoteKeys();
                for (RemoteKey rk: mRemoteKeys){
                    if (rk.getRemoteKeyName().equals("Power"))
                    {
                        data = rk.getRemoteKeyValues();
                    }
                }
                writeData(data);
            }
        });

        channelUpButton = (ImageButton) findViewById(R.id.btn_forward);
        channelUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String data = "";
                for (RemoteKey rk: mRemote.getRemoteKeys()){
                    if (rk.getRemoteKeyName().equals("Channel UP")){
                        data = rk.getRemoteKeyValues();
                    }
                }
                writeData(data);
            }
        });
        channelDownButton = (ImageButton) findViewById(R.id.btn_backward);
        channelDownButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String data = "";
                for (RemoteKey rk: mRemote.getRemoteKeys()){
                    if (rk.getRemoteKeyName().equals("Channel DOWN")){
                        data = rk.getRemoteKeyValues();
                    }
                }
                writeData(data);
            }
        });

        volumeUpButton= (ImageButton) findViewById(R.id.btn_increase);
        volumeUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String data = "";
                for (RemoteKey rk: mRemote.getRemoteKeys()){
                    if (rk.getRemoteKeyName().equals("Volume UP")){
                        data = rk.getRemoteKeyValues();
                    }
                }
                writeData(data);
            }
        });
        volumeDownButton = (ImageButton) findViewById(R.id.btn_decrease);
        volumeDownButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String data = "";
                for (RemoteKey rk: mRemote.getRemoteKeys()){
                    if (rk.getRemoteKeyName().equals("Volume DOWN")){
                        data = rk.getRemoteKeyValues();
                    }
                }
                writeData(data);
            }
        });
        connectBluetooth();
    }


    /**
     * Launch the Bluetooth thread.
     */
    public void connectBluetooth() {
        Log.v(TAG, "Bluetooth connected...");

        // Only one thread at a time
        if (btt != null) {
            Log.w(TAG, "Already connected!");
            return;
        }

        // Initialize the Bluetooth thread, passing in a MAC address
        // and a Handler that will receive incoming messages
        btt = new BluetoothThread(address, new Handler() {

            @Override
            public void handleMessage(Message message) {

                String s = (String) message.obj;

                // Do something with the message
                if (s.equals("CONNECTED")) {
                    Toast.makeText(RemoteActivity.this, "Connection stable", Toast.LENGTH_SHORT).show();

                } else if (s.equals("DISCONNECTED")) {
                    Toast.makeText(RemoteActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();

                } else if (s.equals("CONNECTION FAILED")) {
                    Toast.makeText(RemoteActivity.this, "Connection failed", Toast.LENGTH_LONG).show();

                } else if (s.equals("ADAPTER 404")){
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                } else {

                }
            }
        });

        // Get the handler that is used to send messages
        writeHandler = btt.getWriteHandler();

        // Run the thread
        btt.start();

    }

    /**
     * Kill the Bluetooth thread.
     */
    public void disconnectBluetooth() {
        Log.v(TAG, "Bluetooth Disconnected...");

        if(btt != null) {
            btt.interrupt();
            btt = null;
        }
    }

    /**
     * Send a message using the Bluetooth thread's write handler.
     */
    public void writeData(String data) {
        Log.v(TAG, "Data passed" + data);

        Message msg = Message.obtain();
        msg.obj = data;
        writeHandler.sendMessage(msg);
    }


    /**
     * Kill the thread when we leave the activity.
     */
    protected void onPause() {
        super.onPause();

        disconnectBluetooth();
    }

    @Override
    protected void onResume() {
        super.onResume();

        connectBluetooth();
    }
}
