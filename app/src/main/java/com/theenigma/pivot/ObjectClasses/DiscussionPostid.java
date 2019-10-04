package com.theenigma.pivot.ObjectClasses;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class DiscussionPostid {


        @Exclude
        public String DiscussionPostid;

        public <T extends DiscussionPostid> T withId(@NonNull final String id) {
            this.DiscussionPostid = id;
            return (T) this;
        }

    }
