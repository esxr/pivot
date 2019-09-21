package com.example.icasapp.Notes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends FirestoreRecyclerAdapter<Notes, NotesAdapter.NotesHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private String fileName;
    private ProgressDialog progressBar;


    NotesAdapter(@NonNull FirestoreRecyclerOptions<Notes> options) {
        super(options);
        fileName = "";
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull final NotesHolder notesHolder, final int i, @NonNull final Notes notes) {
        notesHolder.inputName.setText(notes.getFileName());
        notesHolder.semesterInput.setText("SEM:" + notes.getSemester());
        notesHolder.sessionalInput.setText("SES:" + notes.getSessional());
        notesHolder.authorName.setText("AUTHOR:" + notes.getUsername());
        notesHolder.subjectName.setText("SUBJECT:" + notes.getSubject());


//        if (notes.getUsername().equalsIgnoreCase(FirebaseHelper.getUser().getDisplayName())) {
//            notesHolder.deleteButton.setVisibility(View.VISIBLE);
//        } else {
//            notesHolder.deleteButton.setVisibility(View.INVISIBLE);
//        }

        notesHolder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(notes.getDownloadURL()));
                view.getContext().startActivity(i);
            }
        });

        notesHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setIcon(R.drawable.alert)
                        .setTitle("ARE YOU SURE?")
                        .setCancelable(false)
                        .setMessage("This will delete the " + notes.getFileName() + " file forever.")

                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        fileName = notes.getFileName();
                                        progressBar = new ProgressDialog(view.getContext());
                                        initiateDelete();
                                    }
                                }
                        )
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();


            }
        });

    }

    public void initiateDelete(){

        Log.d("msg", "DELETE BUTTON CLICKED:" + fileName);

        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("DELETING...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.setProgress(0);
        progressBar.show();//initially progress is 0
        deleteItem();

    }


    private void deleteItem() {
        //DELETE FIRESTORE
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String TAG = "msg";
        db.collection("NOTES").document(fileName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        progressBar.setProgress(50);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        progressBar.setProgress(100);
                        progressBar.hide();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setProgress(60);
                        progressBar.hide();
                        removeItemFromStorage();
                    }
                });
    }

    private void removeItemFromStorage() {
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Create a reference to the file to delete
        StorageReference deleteRef = storageRef.child("NOTES/" + fileName);

        // Delete the file
        deleteRef
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setProgress(100);
                        progressBar.hide();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        Log.i("msg", fileName + " SUCCESSFULLY DELETED.");
                        progressBar.setProgress(70);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.i("msg", fileName + " FAILED TO DELETE.");
                progressBar.setProgress(100);
                progressBar.hide();
            }
        });
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_cardview_item, parent, false);
        return new NotesHolder(v);
    }

    class NotesHolder extends RecyclerView.ViewHolder {
        TextView inputName;
        TextView sessionalInput;
        TextView semesterInput;
        TextView authorName;
        TextView subjectName;
        Button downloadButton;
        Button deleteButton;
        CardView cardView;

        NotesHolder(@NonNull View itemView) {
            super(itemView);
            inputName = itemView.findViewById(R.id.inputFileName);
            semesterInput = itemView.findViewById(R.id.semesterInput);
            sessionalInput = itemView.findViewById(R.id.sessionalInput);
            authorName = itemView.findViewById(R.id.authorName);
            downloadButton = itemView.findViewById(R.id.downloadButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            subjectName = itemView.findViewById(R.id.subjectInput);
            cardView = itemView.findViewById(R.id.notes_card);
        }
    }
}

