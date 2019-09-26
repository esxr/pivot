package com.example.icasapp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Faculty {
    public static Faculty faculty = new Faculty();
    private String email;
    private List<String> subjects;
    private String interests;
    private String name;
    private String workNumber;
    private String freeTimings;
    private String cabinLocation;
    private String userType;
    private String buffer;
    private String downloadURL;

    public Faculty() {
    }

    public static Faculty getFaculty() {
        return faculty;
    }

    public static void setFaculty(Faculty faculty) {
        Faculty.faculty = faculty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getCabinLocation() {
        return cabinLocation;
    }

    public void setCabinLocation(String cabinLocation) {
        this.cabinLocation = cabinLocation;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getFreeTimings() {
        return freeTimings;
    }

    public void setFreeTimings(String freeTimings) {
        this.freeTimings = freeTimings;
    }


    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }
}
