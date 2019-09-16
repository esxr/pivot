package com.example.icasapp.User;

import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {

    // Alumni
    public String email, educationParagraph, interests, name, workNumber;
    private String userType, buffer, downloadURL;

    // Faculty
    public String subjectsOfConcern, freeTimings, cabinLocation;

    // Student
    public String stream, semester, regNo;


    public User(Map<String, Object> object) {
        for (Field f : getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
//                Log.e("mfc", f.getName()+":"+(String) object.get(f.getName()));
                f.set(this, object.get(f.getName()));

            } catch (IllegalAccessException e) { Log.e("mfc", e.getMessage()+""); }
        }
        Log.e("mfc", this.fetchList().toString());
    }

    public static String typeOfUser(Map<String, Object> object) {
        return (String) object.get("userType");
    }

    public List<List<String>> fetchList() {
        List<List<String>> list = new ArrayList<>();

        for (Field f : getClass().getFields()) {
            List<String> l = new ArrayList<>();
            l.add(f.getName());
            try {
                if(f.get(this) != null ) l.add((String) f.get(this));
                else continue;
            }
            catch(Exception e) {
                continue;
            }
            list.add(l);
        }
        return list;
    }

    public String getProfilePhoto() {
        return this.downloadURL;
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

    public String getSubjectsOfConcern() {
        return subjectsOfConcern;
    }

    public void setSubjectsOfConcern(String subjectsOfConcern) {
        this.subjectsOfConcern = subjectsOfConcern;
    }

    public String getFreeTimings() {
        return freeTimings;
    }

    public void setFreeTimings(String freeTimings) {
        this.freeTimings = freeTimings;
    }

    public String getCabinLocation() {
        return cabinLocation;
    }

    public void setCabinLocation(String cabinLocation) {
        this.cabinLocation = cabinLocation;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
}