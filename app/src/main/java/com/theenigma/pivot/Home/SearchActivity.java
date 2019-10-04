package com.theenigma.pivot.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.theenigma.pivot.Firebase.FirebaseHelper;
import com.theenigma.pivot.MainActivity;
import com.theenigma.pivot.Profile.ProfileAdapter;
import com.theenigma.pivot.Profile.ProfileDisplayActivity;
import com.theenigma.pivot.R;
import com.theenigma.pivot.User.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    ListView listView; ArrayList items; ProfileAdapter itemsAdapter;
    AutoCompleteTextView query; FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setListView();
        setProfileSearch();
    }

    private void setListView() {
        // intitialize the array and listview adapter
        items = new ArrayList<>();
        listView = (ListView) findViewById(R.id.profile_menu);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        itemsAdapter = new ProfileAdapter(getApplicationContext(), items);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent(getApplicationContext(), ProfileDisplayActivity.class);
                    intent.putExtra("user", (User) listView.getItemAtPosition(position));
                    startActivity(intent); } catch(Exception e) { Log.e("mfc", e.getMessage()+""); }
            }
        });
    }

    private void setProfileSearch() {
        query =  findViewById(R.id.query);

        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("USER").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {

                    autoComplete.add(documentSnapshot.get("name").toString());
                }
            }
        });

        query.setAdapter(autoComplete);

        Button profileSearch = (Button) findViewById(R.id.profileSearch);
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
                                    items.add(new User(obj));
                                    Log.i("Downloaded List Item", obj.toString());
                                }
                                itemsAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
