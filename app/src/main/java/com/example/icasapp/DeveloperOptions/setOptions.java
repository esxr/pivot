package com.example.icasapp.DeveloperOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class setOptions extends AppCompatActivity {

    String option;
    String semester;
    String stream;
    FirebaseFirestore firebaseFirestore;
    EditText count;
    Boolean firstTime = true;
    DocumentReference documentReference;
    Boolean alreadyExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_options);

        option = getIntent().getStringExtra("option");

        final ArrayList subs = new ArrayList();

        firebaseFirestore = FirebaseFirestore.getInstance();

        final LinearLayout subjects =  findViewById(R.id.subjects);
        final LinearLayout a = new LinearLayout(this);
        a.setOrientation(LinearLayout.VERTICAL);
        subjects.addView(a);

        Button addField = findViewById(R.id.addField);
        Button applyChanges = findViewById(R.id.apply);




        addField.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Boolean incorrentRequest = false;
                                            if(!firstTime) {
                                                if(count.getText().toString().equals("Enter subject"))
                                                {
                                                    Toast.makeText(getApplicationContext(),"Enter value first",Toast.LENGTH_LONG).show();
                                                    incorrentRequest = true;
                                                }
                                                else
                                                subs.add(count.getText());
                                            }
                                                if(!incorrentRequest) {
                                                    count = new EditText(getApplicationContext()); // Pass it an Activity or Context
                                                    count.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                                    a.addView(count);
                                                    count.setText("Enter subject");
                                                    firstTime = false;
                                                }
                                        }
                                    });

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subs.add(count.getText());
                firebaseFirestore.collection("Specific").whereEqualTo("stream", stream).whereEqualTo("semester",semester).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if( task.getResult().isEmpty())
                       {
                           Map<String, Object> postMap = new HashMap<>();
                           postMap.put("stream", stream);
                           postMap.put("semester", semester);
                           firebaseFirestore.collection("Specific").add(postMap);
                           Toast.makeText(getApplicationContext(),"Document didnt exist. Enter fields again",Toast.LENGTH_LONG).show();
                       }
                       else {
                           for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                  documentReference = documentSnapshot.getReference();
                           }
                           documentReference.collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                   for(DocumentSnapshot documentSnapshot : task.getResult())
                                   {
                                       documentSnapshot.getReference().delete();
                                   }
                                   Log.i("LOLLL",subs.toString());
                                   Map map = new HashMap();
                                   map.put("subjects", subs);

                                   for(Object s : subs){
                                       setDocument(s);
                                   }
                               }
                           });
                       }
                    }
                });

            }
        });


        final Spinner semesterSpinner = findViewById(R.id.semester);
        final Spinner streamSpinner = findViewById(R.id.stream);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.semester_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        semesterSpinner.setAdapter(adapter);

        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                      @Override
                                                      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                          semester = semesterSpinner.getSelectedItem().toString();
                                                      }

                                                      @Override
                                                      public void onNothingSelected(AdapterView<?> parent) {
                                                          semester = semesterSpinner.getSelectedItem().toString();
                                                      }
                                                  });


                ArrayAdapter < CharSequence > adapterStream = ArrayAdapter.createFromResource(this,
                        R.array.stream_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterStream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        streamSpinner.setAdapter(adapterStream);

        streamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stream = streamSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stream = streamSpinner.getSelectedItem().toString();
            }
        });

    }
    void setDocument(Object s)
    {
        Map < String, Object > postMap = new HashMap<>();
        postMap.put("active", "yes");
        postMap.put("alreadyExist",alreadyExist);
        documentReference.collection("Subjects").document(s.toString()).set(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setArrayList(documentReference);
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }
        });
    }
    void setArrayList(final DocumentReference documentReference)
    {
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final ArrayList arrayList = new ArrayList();

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                documentReference.collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            arrayList.add(documentSnapshot.getId());
                        }
                        HashMap map = new HashMap();
                        map.put("subjects",arrayList);
                        documentReference.set(map,SetOptions.merge());
                       firebaseFirestore.collection("Specific").document("parent").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                               HashMap map = (HashMap) task.getResult().getData();
                               for(Object sub : arrayList) {
                                   if(map.containsKey(sub.toString()))
                                   {
                                       documentReference.collection("Subjects").document(sub.toString()).update("alreadyExist",true);
                                   }
                                   else{
                                       HashMap map2 = new HashMap();
                                       map2.put(sub.toString(),documentReference.getId());
                                       task.getResult().getReference().set(map2,SetOptions.merge());
                                   }

                               }
                           }
                       });

                    }
        });


            }
        });
    }
}

