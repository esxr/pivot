package com.example.icasapp.User;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

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
                Log.e("mfc", f.getName()+":"+(String) object.get(f.getName()));
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
}