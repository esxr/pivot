package com.example.icasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimeTableDisplay extends AppCompatActivity {

    List<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_display);

        final ImageView timeTable = findViewById(R.id.time_table);
        list = new ArrayList<String>();

        FirebaseHelper.getUserDetails(FirebaseHelper.getUser().getUid(), new FirebaseHelper.CallbackObject<Map<String, Object>>() {
            @Override
            public void callbackCall(Map<String, Object> object) {
                User user = new User(object);
                Log.e("mfc", "working fine till here");
//                if(!user.getUserType().equals("student")) return; // for now only students can see the timetable

                String sem = user.getSemester();
                String stream = user.getStream();

                char[] alphabet = {'A', 'B', 'C', 'D', 'E'};
                final List<String> downloadURLS = new ArrayList<>();
                StorageReference storageRef = FirebaseStorage.getInstance("gs://icas-phase-1.appspot.com/").getReference();
                for(int i=0; i<5; i++) {
                    try {
                            final int x = i;
                            storageRef
//                                .child("timetables/NoneA1.png")
                            .child("timetables/" + stream + alphabet[i] + sem + ".png")
                            .getDownloadUrl()
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()) {
                                        Log.e("mfc", task.getResult().toString());
                                        downloadURLS.add(task.getResult().toString());
                                        showTimeTable(downloadURLS);
                                    }
                                }
                            });
                    } catch(Exception e) {
                         Log.e("mfc", "lolwa: "+e.getMessage());
                    }
                }
                Log.e("mfc", downloadURLS.toString());
            }
        });


    }

    void showTimeTable(List<String> downloadURLS) {
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.time_table_list);

        // Layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view;

        for (int i=0; i<downloadURLS.size(); i++) {
            ImageView timetable = new ImageView(getApplicationContext());
            Glide.with(this).load(downloadURLS.get(i)).into(timetable);
            parentLayout.addView(timetable);
        }

    }
}
