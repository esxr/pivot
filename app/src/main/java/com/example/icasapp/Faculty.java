package com.example.icasapp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Faculty {
    public static Faculty faculty = new Faculty();
    private String email;
    private String subjectsOfConcern;
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

    public List<List<String>> fetchList() {
        List<List<String>> list = new ArrayList<>();

        for (Field f : getClass().getDeclaredFields()) {
            List<String> l = new ArrayList<>();
            l.add(f.getName());
            try {
                l.add((String) f.get(this));
            } catch (Exception e) {
                l.add("undef");
            }
        }
        return list;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubjectsOfConcern() {
        return subjectsOfConcern;
    }

    public void setSubjectsOfConcern(String subjectsOfConcern) {
        this.subjectsOfConcern = subjectsOfConcern;
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
}
