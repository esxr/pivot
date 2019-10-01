package com.example.icasapp.Auth;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import static com.example.icasapp.Alumni.alumni;
import static com.example.icasapp.Faculty.faculty;
import com.example.icasapp.Alumni;
import com.example.icasapp.Auth.FacultyAuth.FacultyContactFragment;
import com.example.icasapp.Faculty;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterCredentialFragment extends Fragment {

    public static String email, password, userType;
    View registerCredentialFragment;
    AppCompatButton nextButton;
    FormHelper formHelper;
    EditText inputEmail;
    EditText inputPassword;
    FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private CollectionReference alumniQuery, facultyQuery;
    boolean isValid = false;


    public RegisterCredentialFragment() {
        // Required empty public constructor
    }

    public RegisterCredentialFragment(String userType) {
        // Required empty public constructor
        Log.i("msg", userType);
        RegisterCredentialFragment.userType = userType;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerCredentialFragment = inflater.inflate(R.layout.fragment_register_credential, container, false);

        nextButton = registerCredentialFragment.findViewById(R.id.nextButton);
        inputEmail = registerCredentialFragment.findViewById(R.id.inputEmail);
        inputPassword = registerCredentialFragment.findViewById(R.id.inputPassword);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);

        db = FirebaseFirestore.getInstance();
        alumniQuery = db.collection("AlumAuth");
        facultyQuery = db.collection("FacultyAuthentication");

        formHelper = new FormHelper();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                if (formHelper.validateEmail(email) && formHelper.validatePassword(password)) {
                    Log.i("msg", email);
                    if (userType.equalsIgnoreCase("FACULTY")) {
                        authenticateFaculty();
                    } else {
                        authenticateAlumni();
                    }
                } else {
                    Toast.makeText(registerCredentialFragment.getContext(), "INVALID OR INCOMPLETE CREDENTIALS.", Toast.LENGTH_LONG).show();
                    if (!formHelper.validateEmail(email)) {
                        inputEmail.requestFocus();
                    } else {
                        Toast.makeText(getContext(), "PASSWORD MUST BE OF ATLEAST 6 CHARACTERS.", Toast.LENGTH_LONG).show();
                        inputPassword.requestFocus();
                    }
                }
            }
        });

        return registerCredentialFragment;
    }


    private void updateUI() {
        RegisterProgressActivity.pager.setCurrentItem(1);
        RegisterProgressActivity.stepView.go(1, true);
        RegisterProgressActivity.i++;
    }


    private void authenticateAlumni() {
        Log.i("msg", "ALUM INPUT:" + email);

        progressDialog.show();

        readAlumniData(new AlumniCallback() {
            @Override
            public void onCallback(Alumni alumni) {
                progressDialog.hide();

                if(alumni.getEmail().equals(email))
                    updateUI();
                else
                    Toast.makeText(getContext(), "EMAIL NOT VERIFIED. CONTACT DEV TEAM.", Toast.LENGTH_LONG).show();
            }

        });

    }

    private void authenticateFaculty(){
        Log.i("msg", "FACULTY INPUT" + email);

        progressDialog.show();

        readFacultyData(new FacultyCallback() {
            @Override
            public void onCallback(Faculty faculty) {
                progressDialog.hide();

                if(faculty.getEmail().equals(email)) {
                    updateUI();
                    FacultyContactFragment.inputFullName.setText(faculty.getName());
                    FacultyContactFragment.inputWorkPhoneNumber.setText(faculty.getPhone());
                }else
                    Toast.makeText(getContext(), "EMAIL NOT VERIFIED. CONTACT DEV TEAM.", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void readAlumniData(final AlumniCallback alumniCallback) {
        alumniQuery
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                alumni = documentSnapshot.toObject(Alumni.class);
                            }
                            //CALLBACK
                            alumniCallback.onCallback(alumni);
                        } else {
                            Toast.makeText(getContext(), "ALUMNI EMAIL NOT VERIFIED. CONTACT DEV TEAM.", Toast.LENGTH_LONG).show();
                            progressDialog.hide();
                        }
                    }
                });
    }

    private interface AlumniCallback {
        void onCallback(Alumni alumni);
    }

    private void readFacultyData(final FacultyCallback facultyCallback){
        facultyQuery
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                faculty = documentSnapshot.toObject(Faculty.class);
                                Log.i("msg", "FACULTY  DATA:" + faculty.getSubjects());
                            }
                            //CALLBACK
                            facultyCallback.onCallback(faculty);
                        } else {
                            Toast.makeText(getContext(), "FACULTY EMAIL NOT VERIFIED. CONTACT DEV TEAM.", Toast.LENGTH_LONG).show();
                            progressDialog.hide();
                        }
                    }
                });
    }

    private interface FacultyCallback{
        void onCallback(Faculty faculty);
    }


}
