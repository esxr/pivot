package com.example.icasapp.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.icasapp.Annonations.Hardcoded;
import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.Profile.ProfileAdapter;
import com.example.icasapp.Profile.ProfileDisplayActivity;
import com.example.icasapp.R;
import com.example.icasapp.User.TestUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {
    private String TAG = "mfc";
    private View homeView;

    // listview and adapter
    private ArrayList<TestUser> items;
    private ProfileAdapter itemsAdapter;
    private ListView listView;

    //Search
    private EditText query;

    //toggle
    private Group group;
    private View selfProfile;
    private boolean visible = true;
    private LinearLayout homeV;
    int n = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);

        new Thread(new Runnable() {
            public void run() {
                setSearchToggle();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                setListView();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                setProfileSearch();
            }
        }).start();

     /*   new Thread(new Runnable() {
            public void run() {
                setViewSelfProfile();
            }
        }).start(); */

        return homeView;
    }

    public View getHomeView() {
        return homeView;
    }

    //toggle functionality
    private void toggle() {
        group.setVisibility(visibilityOf(visible));
        homeV = homeView.findViewById(R.id.linearLayout2);
        if(n%2==0){
        homeV.setVisibility(View.GONE);}
        else
            homeV.setVisibility(View.VISIBLE);

//        selfProfile.setVisibility(visibilityOf(!visible));
        n++;
        Log.i("MSGGDS",String.valueOf(n));
        visible = !visible;
    }
    private int visibilityOf(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    // sending intent to view self profile
    private void sendIntent() {
        Log.d(TAG, "sendIntent() Triggered");
        FirebaseHelper.getUserDetails(
                FirebaseHelper.getUser().getUid(),
                new FirebaseHelper.CallbackObject<Map<String, Object>>() {
                    @Override
                    public void callbackCall(Map<String, Object> object) {
                        Intent intent = new Intent(getActivity(), ProfileDisplayActivity.class);
                        intent.putExtra("user", new TestUser((Map) object));
                        startActivity(intent);
                    }
                }
        );

    }

    //set all functionality
    private void setSearchToggle() {
        group = (Group) homeView.findViewById(R.id.group);
//        selfProfile = homeView.findViewById(R.id.selfProfile);
        ImageButton searchInitButton = homeView.findViewById(R.id.initSearch);
        searchInitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    private void setViewSelfProfile() {
        Button profileToggle = homeView.findViewById(R.id.selfProfileView);
        profileToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent();
            }
        });
    }

    private void setListView() {
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
    }

    private void setProfileSearch() {
        query = (EditText) homeView.findViewById(R.id.query);
        Button profileSearch = (Button) homeView.findViewById(R.id.profileSearch);
        profileSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (query.length() == 0) return;
                String queryValue =
                        query.getText().toString().trim();
                FirebaseHelper.getDocumentFromCollectionWhere(
                        "USER",
                        queryValue,
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
    }


}