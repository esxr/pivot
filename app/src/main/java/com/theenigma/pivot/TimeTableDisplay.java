package com.theenigma.pivot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.theenigma.pivot.Firebase.FirebaseHelper;
import com.theenigma.pivot.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

public class TimeTableDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_display);

        FirebaseHelper.getUserDetails(FirebaseHelper.getUser().getUid(), new FirebaseHelper.CallbackObject<Map<String, Object>>() {
            @Override
            public void callbackCall(Map<String, Object> object) {
                try {
                    FirebaseStorage.getInstance("gs://icas-phase-1.appspot.com/")
                            .getReference()
                            .child("timetables/" + new User(object).getStream().toString() + ".pdf")
                            .getDownloadUrl()
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) { Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(task.getResult().toString()));
                                        startActivity(intent);
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e("mfc", "lolwa: "+e.getMessage());
                }
            }
        });
    }

}
