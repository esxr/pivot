package com.example.icasapp.User;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.icasapp.Annonations.Hardcoded;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestUser implements Serializable {
    private String name, semester, stream, regNo;
    private String profilePhoto;
    private String UID;
    private String description;
    private String userType;

    public TestUser(String name, String semester, String stream, String regNo, @Nullable String profilePhoto, String UID) {
        this.name = name;
        this.semester = semester;
        this.stream = stream;
        this.regNo = regNo;
        this.profilePhoto = profilePhoto;
        this.UID = UID;
    }

    // Change this to include new properties
    public TestUser(Map<String, Object> object) {
        this.name = (String) object.get("name");
        this.semester = (String) object.get("semester");
        this.stream = (String) object.get("stream");
        this.regNo = (String) object.get("regNo");
        this.profilePhoto = (String) object.get("downloadURL");
        this.UID = (String) object.get("UID");
        this.description = (String) object.get("description");
        this.userType = (String) object.get("userType");
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public HashMap<String, String> getFirebaseDocument() {
        return new HashMap<String, String>() {{
            put("name", getName());
            put("regNo", getRegNo());
            put("semester", getSemester());
            put("stream", getStream());
            put("profile_photo", getProfilePhoto().toString());
            put("UID", getUID());
        }};
    }

    @Hardcoded
    public static HashMap<String, String> getFirebaseDocumentHARDCODED() {
        final String[] streams = {"CSE", "Aero", "Chemical", "ECE", "Mechanical", "Mechatronics"};
        final String[] name = {"Neerav", "Pranav", "Vishal", "Avijit", "Ankur", "Siddhant", "Bakchi", "Bagchii"};
        return new HashMap<String, String>() {{
            put("name", name[new Random().nextInt(name.length)]);
            put("regNo", "181627XXX");
            put("semester", new Random().nextInt(3)+1+"");
            put("stream", streams[new Random().nextInt(streams.length)]);
            put("profile_photo", "https://profilepicturesdp.com/wp-content/uploads/2018/06/generic-user-profile-picture.jpg");
            put("UID", "UID_23461XXX");
        }};
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public HashMap<String, String> getData() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("name", this.getName());
        data.put("semester", this.getSemester());
        data.put("stream", this.getStream());
        return data;
    }
}
