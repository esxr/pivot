package com.example.icasapp.ObjectClasses;


import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;


public class QuestionsId {


    @Exclude
    public String QuestionsId;

    public <T extends QuestionsId> T withId(@NonNull final String id) {
        this.QuestionsId = id;
        return (T) this;
    }

}
