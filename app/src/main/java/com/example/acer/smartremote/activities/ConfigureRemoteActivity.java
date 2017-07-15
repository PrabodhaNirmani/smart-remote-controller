package com.example.acer.smartremote.activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.smartremote.BluetoothThread;
import com.example.acer.smartremote.Models.Remote;
import com.example.acer.smartremote.Models.RemoteKey;
import com.example.acer.smartremote.Models.RemoteLab;
import com.example.acer.smartremote.R;

import java.util.ArrayList;

public class ConfigureRemoteActivity extends AppCompatActivity {


    // Tag for logging
    private static final String TAG = "RemoteConfigureActivity";

    //Argument for startActivityForResult() in bluetoothadapter connection
    private final int REQUEST_ENABLE_BT = 1;

    //this character has to be sent first to start receiving a character
    private final String READ_ENABLE_CHAR  = "A";

    //this character has to be sent at the end to stop receiving characters
    private final String READ_DISABLE_CHAR = "B";

    // MAC address of remote Bluetooth device
    // Replace this with the address of your own module
    //00:21:13:00:B4:E9
    private final String address = "00:21:13:00:B3:CB";

    // The thread that does all the work
    BluetoothThread btt;
    private static RemoteLab sRemoteLab;

    // Handler for writing messages to the Bluetooth connection
    Handler writeHandler;

    private EditText mRemoteNameEdittext;
    private ImageButton mPowerConfigureButton;
    private ImageButton mChannelUpConfigureButton;
    private ImageButton mChannelDownConfigureButton;
    private ImageButton mVolumeUpConfigurationButton;
    private ImageButton mVolumeDownConfigurationButton;
    private TextView mRemoteReceivedSignalTextField;

    private Button mSaveButton;
    private Button mCancelButton;

    private ImageButton mCurrentButton;

    private ProgressDialog mProgressDialog;

    private ArrayList<RemoteKey> mRemoteKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_remote);

        mRemoteKeys = new ArrayList<RemoteKey>();
        sRemoteLab = RemoteLab.get(getApplicationContext());

        mRemoteNameEdittext = (EditText) findViewById(R.id.etName);

        mRemoteReceivedSignalTextField=(TextView)findViewById(R.id.tvSignal);

        mPowerConfigureButton = (ImageButton) findViewById(R.id.btn_on_off);
        mPowerConfigureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showProgressDialog((ImageButton)findViewById(R.id.btn_on_off));
                writeData(READ_ENABLE_CHAR);

            }
        });

        mChannelUpConfigureButton = (ImageButton) findViewById(R.id.btn_forward);
        mChannelUpConfigureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showProgressDialog((ImageButton)findViewById(R.id.btn_forward));
                writeData(READ_ENABLE_CHAR);
            }
        });

        mChannelDownConfigureButton = (ImageButton) findViewById(R.id.btn_backward);
        mChannelDownConfigureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showProgressDialog((ImageButton)findViewById(R.id.btn_backward));
                writeData(READ_ENABLE_CHAR);
            }
        });

        mVolumeUpConfigurationButton = (ImageButton) findViewById(R.id.btn_increase);
        mVolumeUpConfigurationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showProgressDialog((ImageButton)findViewById(R.id.btn_increase));
                writeData(READ_ENABLE_CHAR);
            }
        });

        mVolumeDownConfigurationButton = (ImageButton) findViewById(R.id.btn_decrease);
        mVolumeDownConfigurationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showProgressDialog((ImageButton)findViewById(R.id.btn_decrease));
                writeData(READ_ENABLE_CHAR);
            }
        });


        mSaveButton = (Button) findViewById(R.id.btnSave);
        mSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if ((mRemoteKeys.size() == 5) && !mRemoteNameEdittext.getText().toString().equals("")){
                    Remote remote=new Remote(mRemoteNameEdittext.getText().toString(), mRemoteKeys);
                    sRemoteLab.addRemote(remote);
                    addRemoteToDb(remote);
                    finish();
                } else if (mRemoteNameEdittext.getText().toString().equals("")){
                    Toast.makeText(ConfigureRemoteActivity.this, "Fill in the remote name", Toast.LENGTH_SHORT).show();
                   // Snackbar.make(view, "Fill in the remote name", Snackbar.LENGTH_LONG)
                         //   .setAction("Action", null).show();
                } else {
                    Toast.makeText(ConfigureRemoteActivity.this, "Configure all the remote buttons", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view, "Configure all the remote buttons", Snackbar.LENGTH_LONG)
                         //   .setAction("Action", null).show();
                }
            }
        });

        mCancelButton = (Button) findViewById(R.id.btnCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Listening for Input");

        connectBluetooth();
    }

    /**
     * Launch the Bluetooth thread.
     */
    public void connectBluetooth() {
        Log.v(TAG, "Bluetooth Connected...");

        // Only one thread at a time
        if (btt != null) {
            Log.w(TAG, "Already connected!");
            return;
        }

        // Initialize the Bluetooth thread, passing in a MAC address
        // and a Handler that will receive incoming messages
        btt = new BluetoothThread(address, new Handler() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void handleMessage(Message message) {

                String s = (String) message.obj;

                // Do something with the message
                if (s.equals("CONNECTED")) {
                    Toast.makeText(ConfigureRemoteActivity.this, "Connection stable", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();

                } else if (s.equals("DISCONNECTED")) {
                    Toast.makeText(ConfigureRemoteActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();

                } else if (s.equals("CONNECTION FAILED")) {
                    Toast.makeText(ConfigureRemoteActivity.this, "Connection failed", Toast.LENGTH_LONG).show();
                    hideProgressDialog();


                } else if (s.equals("ADAPTER 404")){
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    hideProgressDialog();

                } else {
                    receiveConfigureDetails(s);

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
        Log.v(TAG, "Bluetooth Disconnected");

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
       // mRemoteReceivedSignalTextField.setText(msg.toString());
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


    //show a progress dialog box whenever waiting for data
    private void showProgressDialog(ImageButton b){
        if (!mProgressDialog.isShowing()){
            mProgressDialog.show();
        }
        this.mCurrentButton = b;
    }

    //dismiss progress dialog after listening to data
    private void hideProgressDialog(){
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        this.mCurrentButton = null;
        writeData(this.READ_DISABLE_CHAR);
    }

    //method to invoke after receiving configuration data
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void receiveConfigureDetails(String data){
        boolean flag=true;
        for (RemoteKey key:mRemoteKeys){
            if(key.getRemoteKeyName().equals(mCurrentButton.getTransitionName().toString())){
                key.setRemoteKeyValue(data);
                flag=false;
                break;
            }
        }
        if(flag){
            this.mRemoteKeys.add(new RemoteKey(mCurrentButton.getTransitionName(), data));
        }

        Log.i(TAG, mCurrentButton.getTransitionName().toString() + " " + data);
        Log.i(TAG, mCurrentButton.getTransitionName().toString());
        Log.i(TAG, mRemoteKeys.get(0).getRemoteKeyValues());
        mRemoteReceivedSignalTextField.setText(data);
        hideProgressDialog();
    }

    public static void instantiateRemoteLab(){
        Cursor cursor=MainActivity.getDbHandler().getAllDevices();
        String[] keys = {"power", "increase", "decrease", "forward", "backward"};
        if(cursor!=null){
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                ArrayList<RemoteKey> keyList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    RemoteKey key = new RemoteKey(keys[i], cursor.getString(2 + i));
                    keyList.add(key);

                }
                String remoteName = cursor.getString(1);
                Remote remote = new Remote(remoteName, keyList);
                sRemoteLab.addRemote(remote);
            }
        }

    }

    private void addRemoteToDb(Remote remote){
        String power=null;
        String increase=null;
        String decrease=null;
        String forward=null;
        String backward=null;
        for(RemoteKey key:mRemoteKeys){
            switch(key.getRemoteKeyName()){
                case "power":
                    power=key.getRemoteKeyValues();
                    break;
                case "increase":
                    increase=key.getRemoteKeyValues();
                    break;
                case "decrease":
                    decrease=key.getRemoteKeyValues();
                    break;
                case "forward":
                    forward=key.getRemoteKeyValues();
                    break;
                case "backward":
                    backward=key.getRemoteKeyValues();
                    break;
                default:
                    power=null;
                    increase=null;
                    decrease=null;
                    forward=null;
                    backward=null;
            }
        }
        MainActivity.getDbHandler().insertDevice(remote.getRemoteBrand(),power,increase, decrease,forward, backward);

    }

}
