package com.example.acer.smartremote.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.acer.smartremote.DatabaseHelper;
import com.example.acer.smartremote.R;

public class MainActivity extends AppCompatActivity {

    private static DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new DatabaseHelper(MainActivity.this);
        ConfigureRemoteActivity.instantiateRemoteLab();
    }

    public void addDevicePressed(View v){
        Intent i = new Intent(MainActivity.this, ConfigureRemoteActivity.class);
        startActivity(i);

    }

    public static DatabaseHelper getDbHandler(){
        return dbHelper;
    }
}
