package com.example.icasapp.Notes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.icasapp.R;

public class NotesForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_form);

        Spinner semesterSpinner = (Spinner) findViewById(R.id.semesterSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSemester = ArrayAdapter.createFromResource(this,
                R.array.semester_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        semesterSpinner.setAdapter(adapterSemester);

        Spinner sessionalSpinner = (Spinner) findViewById(R.id.semesterSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSessional = ArrayAdapter.createFromResource(this,
                R.array.sessional_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSessional.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sessionalSpinner.setAdapter(adapterSessional);




    }
}
