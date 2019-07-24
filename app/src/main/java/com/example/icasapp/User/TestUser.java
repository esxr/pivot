package com.example.icasapp.User;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TestUser implements Serializable {
    String name, semester, stream, regNo;
    String profilePhoto;

    public TestUser(String name, String semester, String stream, String regNo, @Nullable String profilePhoto) {
        this.name = name;
        this.semester = semester;
        this.stream = stream;
        this.regNo = regNo;
        this.profilePhoto = profilePhoto;
    }

    public TestUser(HashMap<String, String> object) {
        this.name = object.get("name");
        this.semester = object.get("semester");
        this.stream = object.get("stream");
    }

    public TestUser(Map<String, Object> object) {
        this.name = (String) object.get("name");
        this.semester = (String) object.get("semester");
        this.stream = (String) object.get("stream");
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
