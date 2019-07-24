package com.example.icasapp.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.icasapp.Annonations.Hardcoded;
import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.Firebase.Query;
import com.example.icasapp.Profile.ProfileAdapter;
import com.example.icasapp.Profile.ProfileDisplayActivity;
import com.example.icasapp.R;
import com.example.icasapp.User.TestUser;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseUser user;

    View homeView;
    EditText query;
    Button profileSearch;

    // listview and adapter
    ArrayList<TestUser> items;
    ProfileAdapter itemsAdapter;
    ListView listView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);

        // reference of all views
        query = homeView.findViewById(R.id.query);
        profileSearch = homeView.findViewById(R.id.profileSearch);

        // intitialize the array and listview adapter
        items = new ArrayList<>();
        listView = (ListView) homeView.findViewById(R.id.profile_menu);
        itemsAdapter = new ProfileAdapter(getContext(), items);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProfileDisplayActivity.class);
                intent.putExtra("user", (TestUser) listView.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        profileSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] queryParams =
                        query.getText().toString()
                                .trim().split(":");
                FirebaseHelper.getDocumentFromCollectionWhere(
                        new Query(queryParams[0], queryParams[1]),
                        "USER",
                        new FirebaseHelper.CallbackObject<List<HashMap<String, String>>>() {
                            @Override
                            public void callbackCall(List<HashMap<String, String>> object) {
                                items.clear();
                                for(HashMap<String, String> obj : object) { try {
                                    items.add(new TestUser(obj));
                                    itemsAdapter.notifyDataSetChanged();
                                } catch(Exception e) {} }
                            }
                        });
            }
        });

        // Set listview and adapter
        TestUser user = new TestUser(
                "Pranav",
                "",
                "",
                "181627027",
                ""
        );

        @Hardcoded
        Button profileview = homeView.findViewById(R.id.profileview);
        profileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get profilePhoto URI
                String imagePath = "";
                try {
                    imagePath = jugaadTheImage().getAbsolutePath();
                } catch(Exception e) {
                    Log.e("File save problem", ""+e);
                }

                // User
                TestUser sampleUser = new TestUser("Dhooli", "2", "CSE", "181627027", imagePath);

                Intent intent = new Intent(getActivity(), ProfileDisplayActivity.class);
                intent.putExtra("user", sampleUser);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return homeView;
    }

    @Hardcoded public File jugaadTheImage() throws Exception{
        // Image ka jugaad (sasta plx)
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/profilePhotos";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();

        File file = new File(dir, "temp.png");
        FileOutputStream fOut = new FileOutputStream(file);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.avi);
        bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        fOut.flush();
        fOut.close();

        return file;
    }
}