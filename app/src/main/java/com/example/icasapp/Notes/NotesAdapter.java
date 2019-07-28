package com.example.icasapp.Notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.icasapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

import java.util.Collections;
import java.util.List;




public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {


    private ArrayList<String> notesNames;
    private ArrayList<String> semesterNames;
    private ArrayList<String> sessionalNames;
    private ArrayList<String> subjectNames;
    private ArrayList<String> subjectAbr;
    private ArrayList<Uri> DOWNLOAD_URL_LIST, f_DOWNLOAD_URL_LIST;

    private ArrayList<String> fnotesNames;
    private ArrayList<String> fsemesterNames;
    private ArrayList<String> fsessionalNames;
    private ArrayList<String> fsubjectNames;
    private ArrayList<String> fsubjectAbr;

    private List<Integer> indexList;
    public Uri DOWNLOAD_URL;

    FirebaseFirestore db;

    public int in;
    private Context context;


    public boolean isFilterActive;


    NotesAdapter(ArrayList<String> notesNames, ArrayList<String> semesterNames, ArrayList<String> sessionalNames, ArrayList<String> subjectNames, ArrayList<String> subjectAbr, ArrayList<Uri> DOWNLOAD_URL_LIST) {
        this.notesNames = notesNames;
        this.semesterNames = semesterNames;
        this.sessionalNames = sessionalNames;
        this.subjectAbr = subjectAbr;
        this.subjectNames = subjectNames;
        this.DOWNLOAD_URL_LIST = DOWNLOAD_URL_LIST;

        this.fnotesNames = new ArrayList<>();
        this.fsemesterNames = new ArrayList<>();
        this.fsubjectAbr = new ArrayList<>();
        this.fsessionalNames = new ArrayList<>();
        this.fsubjectNames = new ArrayList<>();
        this.f_DOWNLOAD_URL_LIST = new ArrayList<>();


        this.indexList = new ArrayList<>();
        this.isFilterActive = false;
        DOWNLOAD_URL = null;

        db = FirebaseFirestore.getInstance();
    }

    public void setFilter(String searchCriteria) {
        setFilteredItems(searchCriteria);
        isFilterActive = true;
        notifyDataSetChanged();
    }

    public void clearFilter() {
        isFilterActive = false;

        notifyDataSetChanged();
    }

    private void setFilteredItems(String searchCriteria) {
        if (isFilterActive) {
            searchFilter(notesNames, searchCriteria);
            addItem();
        }
    }

    public void searchFilter(ArrayList<String> arrayList, String searchCriteria) {
        if (isFilterActive) {

            indexList = new ArrayList<>();

            for (int i = 0; i < arrayList.size(); i++) {

                String item = arrayList.get(i);
                if (item.toLowerCase().contains(searchCriteria.toLowerCase())) {
                    if (!indexList.contains(arrayList.indexOf(item))) {
                        indexList.add(i);
                    }
                }
            }
        }
    }

    public void addItem() {

        if (isFilterActive) {
            fnotesNames = new ArrayList<>();
            fsessionalNames = new ArrayList<>();
            fsemesterNames = new ArrayList<>();
            fsubjectAbr = new ArrayList<>();
            fsubjectNames = new ArrayList<>();
            f_DOWNLOAD_URL_LIST = new ArrayList<>();

            Collections.sort(indexList);


            try {

                for (int i = 0; i < indexList.size(); i++) {
                    int index = indexList.get(i);
                    //IN THE REFERENCE OF THIS notesName
                    fnotesNames.add(notesNames.get(index));
                    fsessionalNames.add(sessionalNames.get(index));
                    fsemesterNames.add(semesterNames.get(index));
                    fsubjectNames.add(subjectNames.get(index));
                    fsubjectAbr.add(subjectAbr.get(index));
                    f_DOWNLOAD_URL_LIST.add(DOWNLOAD_URL_LIST.get(index));
                }
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        notifyDataSetChanged();


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notes_cardview_item, viewGroup, false);
        context=viewGroup.getContext();
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        //SET THE TEXT HERE.

        in = i;
        if (isFilterActive) {
            //in = viewHolder.getAdapterPosition();

            DOWNLOAD_URL = f_DOWNLOAD_URL_LIST.get(in);

            viewHolder.notesName.setText(fnotesNames.get(in));
            viewHolder.semesterName.setText("SEMESTER:  " + fsemesterNames.get(in));
            viewHolder.sessionalName.setText("SESSIONAL:  " + fsessionalNames.get(in));
            viewHolder.subjectName.setText("SUBJECT:  " + fsubjectNames.get(in) + "[" + fsubjectAbr.get(in) + "]");

            viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(DOWNLOAD_URL);
                    view.getContext().startActivity(i);
                }
            });

            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    in = i;
                    Log.i("msg", "DELETE BUTTON CLICKED ON:" + fnotesNames.get(in));


                    final int index = notesNames.indexOf(fnotesNames.get(in));


                    db.collection("NOTES").document(notesNames.get(index))

                            .delete()

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("msg", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("msg", "Error deleting document", e);
                                }
                            });


                    // Create a storage reference from our app
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();


                    // Create a reference to the file to delete
                    StorageReference fileRef = storageRef.child("NOTES/" + notesNames.get(index));

                    // Delete the file
                    fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully
                            Log.i("msg", "FILE DELETED:" + notesNames.get(index));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                            Log.i("msg", "FILE DID NOT GET DELETED:" + notesNames.get(index));
                        }
                    });

                    semesterNames.remove(index);
                    sessionalNames.remove(index);
                    subjectAbr.remove(index);
                    subjectNames.remove(index);
                    DOWNLOAD_URL_LIST.remove(index);
                    notesNames.remove(index);

                    fnotesNames.remove(in);
                    fsessionalNames.remove(in);
                    fsemesterNames.remove(in);
                    fsubjectNames.remove(in);
                    fsubjectAbr.remove(in);
                    f_DOWNLOAD_URL_LIST.remove(in);

                    Intent intent = new Intent(context,NotesViewActivity.class);
                    context.startActivity(intent);
                    notifyItemRemoved(in);
                }
            });

        } else {
            try {

                viewHolder.notesName.setText(notesNames.get(in));
                viewHolder.semesterName.setText("SEMESTER:  " + semesterNames.get(in));
                viewHolder.sessionalName.setText("SESSIONAL:  " + sessionalNames.get(in));
                viewHolder.subjectName.setText("SUBJECT:  " + subjectNames.get(in) + "[" + subjectAbr.get(in) + "]"); //sub name and abbreviation.
                viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(DOWNLOAD_URL_LIST.get(in));
                        view.getContext().startActivity(intent);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
                Log.i("LOL","SUCCESS");

            }

            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    in = i;
                    Log.i("msg", "DELETE BUTTON CLICKED ON:" + notesNames.get(in));

                    db.collection("NOTES").document(notesNames.get(in))
                            .delete()

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("msg", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("msg", "Error deleting document", e);
                                }
                            });


                    // Create a storage reference from our app
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();


                    // Create a reference to the file to delete
                    StorageReference fileRef = storageRef.child("NOTES/" + notesNames.get(in));

                    // Delete the file
                    fileRef
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
                                    Log.i("msg", "FILE DELETED.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                    Log.i("msg", "FILE DID NOT GET DELETED:" + notesNames.get(in));
                                }
                            });

                    try {
                        semesterNames.remove(i);
                        sessionalNames.remove(i);
                        subjectAbr.remove(i);
                        subjectNames.remove(i);
                        DOWNLOAD_URL_LIST.remove(i);
                        notesNames.remove(i);
                        notifyItemRemoved(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   Intent intent = new Intent(context,NotesViewActivity.class);
                    ((Activity)context).finish();

                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (isFilterActive) {
            return this.fnotesNames.size();
        }
        return this.notesNames.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView notesName;
        TextView semesterName;
        TextView sessionalName;
        TextView subjectName;
        Button downloadButton;
        Button deleteButton;

        ViewHolder(View itemView) {
            super(itemView);

            notesName =
                    itemView.findViewById(R.id.item_title);
            semesterName =
                    itemView.findViewById(R.id.item_detail);
            sessionalName =
                    itemView.findViewById(R.id.item_detail2);
            subjectName =
                    itemView.findViewById(R.id.item_detail3);
            downloadButton =
                    itemView.findViewById(R.id.downloadButton);
            deleteButton =
                    itemView.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}