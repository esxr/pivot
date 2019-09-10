package com.example.icasapp.Auth.StudentAuth;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

import static com.example.icasapp.GlobalUser.globalUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentRegisterCredentialFragment extends Fragment {

    private View studentRegView;
    AppCompatEditText inputRegNo;
    AppCompatButton nextButton;
    String regNo;
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
               Log.i("msg", user.getName());
               progressDialog.hide();
               UserRegCredentialFragment.inputFullName.setText(user.getName());
               UserRegCredentialFragment.inputEmail.setText(user.getEmail());
               RegisterProgressActivity.pager.setCurrentItem(1);
            }
        });
    }

    private interface FireStoreCallback{
        void onCallback(GlobalUser user);
    }

    private void readData(final FireStoreCallback fireStoreCallback){

        query
                .whereEqualTo("regNo", regNo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
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
