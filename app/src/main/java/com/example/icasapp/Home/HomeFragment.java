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
import android.widget.Toast;

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
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseUser user;

    View homeView;
    EditText query;
    Button profileSearch, generateUsers;

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
        listView.setEmptyView(homeView.findViewById(R.id.emptyElement));
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

        // Hardcoded
        generateUsers = homeView.findViewById(R.id.generateUsers);
        generateUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.generateFakeFirebaseUsers(10);
                Toast.makeText(getContext(), "Generated 10 fake users", Toast.LENGTH_SHORT).show();
            }
        });

        profileSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(query.length() == 0) return;
                String[] queryParams =
                        query.getText().toString()
                                .trim().split(":");
                FirebaseHelper.getDocumentFromCollectionWhere(
                        "USER",
                        new Query(queryParams),
                        new FirebaseHelper.CallbackObject<List<Map<String, Object>>>() {
                            @Override
                            public void callbackCall(List<Map<String, Object>> object) {
                                items.clear();
                                Log.d("Callback", ""+object);
                                for(Map<String, Object> obj : object) {
                                    items.add(new TestUser(obj));
                                }
                                itemsAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });

        // Inflate the layout for this fragment
        return homeView;
    }
}