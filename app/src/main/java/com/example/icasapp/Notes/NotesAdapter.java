package com.example.icasapp.Notes;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;
import android.widget.TextView;

import com.example.icasapp.R;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    FirebaseFirestore db;

    private ArrayList<String> notesNames;
    private ArrayList<String> semesterNames;
    private ArrayList<String> sessionalNames;
    private ArrayList<String> subjectNames;
    private ArrayList<String> subjectAbr;
    private Uri DOWNLOAD_URL;

    NotesAdapter(ArrayList<String> notesNames, ArrayList<String> semesterNames, ArrayList<String> sessionalNames, ArrayList<String> subjectNames, ArrayList<String> subjectAbr, Uri DOWNLOAD_URL)
    {
        this.notesNames = notesNames;
        this.semesterNames = semesterNames;
        this.sessionalNames = sessionalNames;
        this.subjectAbr = subjectAbr;
        this.subjectNames = subjectNames;
        this.DOWNLOAD_URL = DOWNLOAD_URL;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notes_cardview_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //SET THE TEXT HERE.

        viewHolder.notesName.setText(notesNames.get(i));
        viewHolder.semesterName.setText("SEMESTER:  "+semesterNames.get(i));
        viewHolder.sessionalName.setText("SESSIONAL:  "+sessionalNames.get(i));
        viewHolder.subjectName.setText("SUBJECT:  "+subjectNames.get(i)+"["+subjectAbr.get(i)+"]"); //sub name and abbreviation.

        viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(DOWNLOAD_URL);
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesNames.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView notesName;
        TextView semesterName;
        TextView sessionalName;
        TextView subjectName;
        Button downloadButton;

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

        }
    }





}