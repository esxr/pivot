package com.example.icasapp.Notes;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.icasapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewNotesActivity extends AppCompatActivity {


    ListView listView;

    List<DocumentHelper> documentHelperList;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        listView     = findViewById(R.id.listView);

        documentHelperList = new ArrayList<DocumentHelper>();

        viewAllFiles();

    }

    private void viewAllFiles(){

    databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                DocumentHelper documentHelper = postSnapshot.getValue(DocumentHelper.class);
                documentHelperList.add(documentHelper);

            }

            String uploads[] = new String[documentHelperList.size()];

            for(int i = 0; i < uploads.length ; i++){
                uploads[i] = documentHelperList.get(i).getName();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext() , android.R.layout.simple_list_item_1 , uploads){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =  super.getView(position, convertView, parent);
                    TextView listText = view.findViewById(android.R.id.text1);
                    listText.setTextColor(Color.BLACK);

                    return view;
                }
            };

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DocumentHelper dHelper = documentHelperList.get(position);

                    Intent intent = new Intent();
                    intent.setType(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(dHelper.getUrl()));
                    startActivity(intent);
                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


}
}

