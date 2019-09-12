package com.example.icasapp.Home;

import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.Firebase.Query;
import com.example.icasapp.Profile.ProfileAdapter;
import com.example.icasapp.Profile.ProfileDisplayActivity;
import com.example.icasapp.R;
import com.example.icasapp.User.TestUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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

    ImageView profileImage;

    Spinner querySpinner;

    String queryProperty;

    boolean visible = true;

    public HomeFragment() {
        // Required empty public constructor
    }

    public String getQueryProperty() {
        return queryProperty;
    }

    public void setQueryProperty(String queryProperty) {
        this.queryProperty = queryProperty;
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

        // initialize the user profile image
//        profileImage = homeView.findViewById(R.id.userProfileImage);
//        FirebaseHelper.findDocumentWithUID(
//                FirebaseHelper.getUser().getUid(),
//                new FirebaseHelper.CallbackObject<String>() {
//                    @Override
//                    public void callbackCall(String docID) {
//                        // get the image from docId
//                        FirebaseHelper.getFirestore()
//                                .collection("USER")
//                                .document(docID).get()
//                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                        String imagePath = (String) task.getResult().get("downloadURL");
//
//                                        // now display the image
//                                        Glide.with(getContext())
//                                                .load(Uri.parse(imagePath))
//                                                .into(profileImage);
//                                    }
//                                });
//                    }
//                }
//        );

//        String imageUri = "drawable://" + R.drawable.avi;
//        Glide.with(getContext())
//                                                .load(imageUri)
//                                                .into(profileImage);

        ImageButton searchInitButton = homeView.findViewById(R.id.initSearch);
        searchInitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

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

        //
        ArrayAdapter<CharSequence> adapterStream = ArrayAdapter.createFromResource(getContext(),
                R.array.query_property_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterStream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        querySpinner = (Spinner) homeView.findViewById(R.id.querySpinner);
        querySpinner.setAdapter(adapterStream);

        querySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int position, long id) {
                Log.v("SpinnerSelected Item",
                        "" + querySpinner.getSelectedItem());
                Log.v("Clicked position", "" + position);
                setQueryProperty(querySpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Log.v("NothingSelected Item",
                        "" + querySpinner.getSelectedItem().toString());
                setQueryProperty(querySpinner.getSelectedItem().toString());

            }
        });

        profileSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (query.length() == 0) return;
                String queryValue =
                        query.getText().toString().trim();
                FirebaseHelper.getDocumentFromCollectionWhere(
                        "USER",
                        new Query(getQueryProperty(), queryValue),
                        new FirebaseHelper.CallbackObject<List<Map<String, Object>>>() {
                            @Override
                            public void callbackCall(List<Map<String, Object>> object) {
                                items.clear();
                                Log.d("Callback", "" + object);
                                for (Map<String, Object> obj : object) {
                                    items.add(new TestUser(obj));
                                    Log.i("Downloaded List Item", obj.toString());
                                }
                                itemsAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });
        // Inflate the layout for this fragment
        return homeView;
    }

    void toggle() {
//        profileImage.setVisibility(visibilityOf(!visible));

        homeView.findViewById(R.id.search).setVisibility(visibilityOf(visible));
        profileSearch.setVisibility(visibilityOf(visible));
        visible = !visible;
    }

    int visibilityOf(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }
}