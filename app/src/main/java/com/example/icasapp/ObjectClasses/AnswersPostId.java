package com.example.icasapp.ObjectClasses;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class AnswersPostId {


    @Exclude
    public String AnswerPostId;

    public <T extends AnswersPostId> T withId(@NonNull final String id) {
        this.AnswerPostId = id;
        return (T) this;
    }
}
