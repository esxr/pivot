package com.example.icasapp.Notes;

import android.content.Intent;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;

import android.widget.TextView;

import com.example.icasapp.R;


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
                    String notesName = notesNames.get(index);
                    NotesAbstraction notesAbstraction = new NotesAbstraction();
                    //IN THE REFERENCE OF THIS notesName
                    notesAbstraction.setNotesName(notesName);

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
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        //SET THE TEXT HERE.

        if (isFilterActive) {

            DOWNLOAD_URL = f_DOWNLOAD_URL_LIST.get(i);

            viewHolder.notesName.setText(fnotesNames.get(i));
            viewHolder.semesterName.setText("SEMESTER:  " + fsemesterNames.get(i));
            viewHolder.sessionalName.setText("SESSIONAL:  " + fsessionalNames.get(i));
            viewHolder.subjectName.setText("SUBJECT:  " + fsubjectNames.get(i) + "[" + fsubjectAbr.get(i) + "]");

            viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(DOWNLOAD_URL);
                    view.getContext().startActivity(i);
                }
            });

        } else {


            viewHolder.notesName.setText(notesNames.get(i));
            viewHolder.semesterName.setText("SEMESTER:  " + semesterNames.get(i));
            viewHolder.sessionalName.setText("SESSIONAL:  " + sessionalNames.get(i));
            viewHolder.subjectName.setText("SUBJECT:  " + subjectNames.get(i) + "[" + subjectAbr.get(i) + "]"); //sub name and abbreviation.
            viewHolder.downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(DOWNLOAD_URL_LIST.get(i));
                    view.getContext().startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (isFilterActive) {
            return fnotesNames.size();
        }
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}