package com.theenigma.pivot.Auth.AlumniAuth;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.theenigma.pivot.Alumni;
import com.theenigma.pivot.Auth.FormHelper;
import com.theenigma.pivot.Auth.RegisterProgressActivity;
import com.theenigma.pivot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlumniContactFragment extends Fragment {


    public AlumniContactFragment() {
        // Required empty public constructor
    }

    View view;
    AppCompatButton nextButton;
    AppCompatEditText inputName, inputWorkNumber;
    FormHelper formHelper;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alumni_contact, container, false);

        inputName = view.findViewById(R.id.inputFullName);
        inputWorkNumber = view.findViewById(R.id.inputWorkNumber);
        nextButton = view.findViewById(R.id.nextButton);
        formHelper = new FormHelper();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString().trim();
                String workNumber = inputWorkNumber.getText().toString().trim();
                if(formHelper.validateField(name) && formHelper.validateField(workNumber)){
                    Alumni.alumni.setName(name);
                    Alumni.alumni.setWorkNumber(workNumber);
                    RegisterProgressActivity.stepView.go(2,true);
                    RegisterProgressActivity.i++;
                    RegisterProgressActivity.pager.setCurrentItem(2);
                }else{
                    Toast.makeText(getContext(), "FIELD(S) ARE EMPTY.PLEASE FILL.", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }

}
