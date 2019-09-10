package com.example.icasapp.Auth.StudentAuth;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.example.icasapp.Auth.FormHelper;
import com.example.icasapp.Auth.RegisterProgressActivity;
import com.example.icasapp.GlobalUser;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.icasapp.GlobalUser.globalUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentRegisterCredentialFragment extends Fragment {

    private View studentRegView;
    AppCompatEditText inputRegNo;
    AppCompatButton nextButton;
    String name, email, regNo;
    String[] emailList;
    FormHelper formHelper;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    CollectionReference query;


    public StudentRegisterCredentialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        studentRegView = inflater.inflate(R.layout.fragment_student_register_credential, container, false);

        nextButton = studentRegView.findViewById(R.id.nextButton);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);


        inputRegNo = studentRegView.findViewById(R.id.inputRegNo);


        db = FirebaseFirestore.getInstance();
        query = db.collection("Authentication");

        formHelper = new FormHelper();
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regNo = inputRegNo.getText().toString();
                if (!formHelper.validateRegNo(regNo)) {
                    Toast.makeText(view.getContext(), "INVALID REGISTRATION NUMBER", Toast.LENGTH_LONG).show();
                } else {
                    authenticateRegNo();
                }
            }
        });


        return studentRegView;
    }

    private void authenticateRegNo() {
        Log.i("msg", "USER INPUT:" + regNo);

        progressDialog.show();


        readData(new FireStoreCallback() {
            @Override
            public void onCallback(GlobalUser user) {
                progressDialog.hide(); //HIDE PROGRESS BAR

                name = user.getName();
                email = user.getEmail();


                if (email.contains(";")) {
                    emailList = email.split(";");
                    Log.i("msg", emailList[0] + "#" + emailList[1]);

                    new AlertDialog.Builder(getContext())
                            .setCancelable(false)
                            .setTitle("We have more than one mail of yours associated with ICAS:")
                            .setSingleChoiceItems(emailList, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    email = emailList[i];
                                }
                            })
                            .setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();
                                    store();
                                    updateUI();

                                }
                            })
                            .create()
                            .show();
                }else{
                    store();
                    updateUI();
                }



            }
        });
    }

    public void store(){
        globalUser.setEmail(email);
        globalUser.setName(name);
        globalUser.setUserType("STUDENT");
    }

    public void updateUI(){
        UserRegCredentialFragment.inputFullName.setText(name);
        UserRegCredentialFragment.inputEmail.setText(email);
        RegisterProgressActivity.pager.setCurrentItem(1);
    }

    private interface FireStoreCallback {
        void onCallback(GlobalUser user);
    }

    private void readData(final FireStoreCallback fireStoreCallback) {

        query
                .whereEqualTo("regNo", regNo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                globalUser = documentSnapshot.toObject(GlobalUser.class);
                            }
                            //CALLBACK
                            fireStoreCallback.onCallback(globalUser);
                        }
                    }
                });

    }


}
