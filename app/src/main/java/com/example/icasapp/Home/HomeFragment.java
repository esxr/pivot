package com.example.icasapp.Home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.icasapp.Firebase.FirebaseHelperKotlin;
import com.example.icasapp.Menu_EditProfile.EditProfileActivity;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import io.reactivex.annotations.NonNull;

/**

 * A simple {@link Fragment} subclass.

 */

public class HomeFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseUser user;

    View homeView;

    TextView info;
    EditText query;
    Button profileSearch;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);

        // reference of all views
        info = homeView.findViewById(R.id.info);
        query = homeView.findViewById(R.id.query);
        profileSearch = homeView.findViewById(R.id.profileSearch);

        profileSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hello = FirebaseHelperKotlin.Companion
                        .getDocumentFromCollection("sampleDocument", "user");

                Log.d("LOL", ""+hello);
            }
        });

        // Inflate the layout for this fragment
        return homeView;
    }
}