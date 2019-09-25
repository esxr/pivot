package com.example.icasapp;

public class GlobalState {
    public static GlobalState globalState = new GlobalState();
    public  String userType;
    public  boolean internetConnection;
    public  boolean isSignedIn;
    public  String buffer = null;
    public  String name;
    public  String downloadURL;

    public GlobalState() {
    }


    public static GlobalState getGlobalState() {
        return globalState;
    }

    public static void setGlobalState(GlobalState globalState) {
        GlobalState.globalState = globalState;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isInternetConnection() {
        return internetConnection;
    }

    public void setInternetConnection(boolean internetConnection) {
        this.internetConnection = internetConnection;
    }

    public boolean isSignedIn() {
        return isSignedIn;
    }

    public void setSignedIn(boolean signedIn) {
        isSignedIn = signedIn;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }
}
