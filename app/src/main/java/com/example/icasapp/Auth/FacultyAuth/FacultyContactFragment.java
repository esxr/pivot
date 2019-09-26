package com.example.icasapp.Auth.FacultyAuth;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.example.icasapp.Auth.FormHelper;
import com.example.icasapp.Auth.RegisterCredentialFragment;
import com.example.icasapp.Auth.RegisterProgressActivity;
import com.example.icasapp.Faculty;
import com.example.icasapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyContactFragment extends Fragment {


    View view;
    AppCompatEditText inputFullName, inputWorkPhoneNumber, inputCabinLocation, inputFreeTimings;
    AppCompatButton nextButton;

    public FacultyContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_faculty_contact, container, false);
        final FormHelper formHelper = new FormHelper();


        String email = RegisterCredentialFragment.email;
        Faculty.faculty.setEmail(email);
        Log.i("msg", "EMAIL FACUL:" + email + "=" + Faculty.faculty.getEmail());

        inputFullName = view.findViewById(R.id.inputFullName);
        inputWorkPhoneNumber = view.findViewById(R.id.inputWorkNumber);
        inputCabinLocation = view.findViewById(R.id.inputCabinLocation);
        inputFreeTimings = view.findViewById(R.id.inputFreeTimings);
        nextButton = view.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name, workPhoneNumber = "", cabinLocation, freeTimings;

                name = inputFullName.getText().toString().trim();
                workPhoneNumber = inputWorkPhoneNumber.getText().toString().trim();
                cabinLocation = inputCabinLocation.getText().toString().trim();
                freeTimings = inputFreeTimings.getText().toString().trim();

                if (formHelper.validateField(freeTimings) && formHelper.validateField(cabinLocation)){
                    Faculty.faculty.setFreeTimings(freeTimings);
                    Faculty.faculty.setCabinLocation(cabinLocation);
                    Faculty.faculty.setWorkNumber(workPhoneNumber);
                    Faculty.faculty.setName(name);
                    RegisterProgressActivity.pager.setCurrentItem(2);
                    RegisterProgressActivity.stepView.go(2,true);
                    RegisterProgressActivity.i++;
                }else
                    Toast.makeText(getContext(), "FIELDS MISSING.PLEASE FILL IN THE REQUIRED FIELDS.", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

}
