package com.example.icasapp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Alumni {
    public static Alumni alumni = new Alumni();
    private String email;
    private String educationParagraph;
    private String interests;
    private String designation;
    private String name;
    private String workNumber;
    private String userType;
    private String buffer;
    private String downloadURL;

    public Alumni(){}

    public List<List<String>> getList() {
        List<List<String>> list = new ArrayList<>();

        for (Field f : getClass().getDeclaredFields()) {
            List<String> l = new ArrayList<>();
            l.add(f.getName());
            try { l.add((String) f.get(this)); } catch(Exception e) { l.add("undef"); }
        }
        return list;
    }

    public static Alumni getAlumni() {
        return alumni;
    }

    public static void setAlumni(Alumni alumni) {
        Alumni.alumni = alumni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEducationParagraph() {
        return educationParagraph;
    }

    public void setEducationParagraph(String educationParagraph) {
        this.educationParagraph = educationParagraph;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
}
