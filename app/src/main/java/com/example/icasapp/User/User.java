package com.example.icasapp.User;

import java.util.HashMap;

public class User {
    private Credential credential;
    private Privilage privilage;
    private UserType type;
    private HashMap<String, String> userData;

    public User(Credential credential, Privilage privilage) {
        this.credential = credential;
        this.privilage = privilage;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public Privilage getPrivilage() {
        return privilage;
    }

    public void setPrivilage(int level) {
        this.privilage.setLevel(level);
    }

    public String getType() {
        return type.getType();
    }

    public void setType(int typeInt) {
        this.type.setType(typeInt);
    }
}
