package com.theenigma.pivot.University_Info;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theenigma.pivot.ObjectClasses.Uni;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FirebaseUniRecyclerAdapter extends FirestoreRecyclerAdapter<Uni, FirebaseUniRecyclerAdapter.UniAdapter> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseUniRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Uni> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UniAdapter uniAdapter, int i, @NonNull Uni uni) {

    }

    @NonNull
    @Override
    public UniAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    class UniAdapter extends RecyclerView.ViewHolder{

        public UniAdapter(@NonNull View itemView) {
            super(itemView);
        }
    }
}
