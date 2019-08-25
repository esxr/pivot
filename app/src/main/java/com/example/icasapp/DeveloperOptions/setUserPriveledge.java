package com.example.icasapp.DeveloperOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class setUserPriveledge extends AppCompatActivity {
    AutoCompleteTextView setText;
    FirebaseFirestore firebaseFirestore;
    ArrayList names;
    RadioGroup radioGroup;
    Button setPriviledge;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_priveledge);

        setText = findViewById(R.id.searchText);
        names = new ArrayList();
        radioGroup = findViewById(R.id.radioGroup);
        setPriviledge = findViewById(R.id.setPriveledge);

        try {

            final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

            firebaseFirestore = FirebaseFirestore.getInstance();

            firebaseFirestore.collection("USER").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {

                        autoComplete.add(documentSnapshot.get("name").toString());
                    }
                }
            });

            setText.setAdapter(autoComplete);


            // Uncheck or reset the radio buttons initially
            radioGroup.clearCheck();

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    radioButton = group.findViewById(checkedId);
                }
            });

            setPriviledge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name = setText.getText().toString();
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    if (selectedId == -1 || name.equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "Nothing has been selected",
                                Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        firebaseFirestore.collection("USER").whereEqualTo("name", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        documentSnapshot.getReference().update("userType",radioButton.getText());
                                }
                            }
                        });
                    }

                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG);
            Log.e("ERROR",e.getMessage());
        }
    }
}
