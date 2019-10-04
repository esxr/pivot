package com.theenigma.pivot.User;

import android.util.Log;

import androidx.annotation.Nullable;

import com.theenigma.pivot.Annonations.Hardcoded;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TestUser implements Serializable {
    public String name, semester, stream, regNo, description;
    private String profilePhoto;
    private String UID;
    static String TAG = "mfc";

    public TestUser(String name, String semester, String stream, String regNo, @Nullable String profilePhoto, String UID) {
        this.name = name;
        this.semester = semester;
        this.stream = stream;
        this.regNo = regNo;
        this.profilePhoto = profilePhoto;
        this.UID = UID;
    }

    public TestUser(String name, String semester, String stream, String regNo, String profilePhoto, String UID, String description) {
        this.name = name;
        this.semester = semester;
        this.stream = stream;
        this.regNo = regNo;
        this.profilePhoto = profilePhoto;
        this.UID = UID;
        this.description = description;
    }

//    public TestUser(Map<String, Object> object) {
//        Log.e(TAG, "TestUser object: "+object.toString());
//        this.name = (String) object.get("name");
//        this.semester = (String) object.get("semester");
//        this.stream = (String) object.get("stream");
//        this.regNo = (String) object.get("regNo");
//        this.profilePhoto = (String) object.get("downloadURL");
//        this.UID = (String) object.get("UID");
//        this.description = (String) object.get("description");
//    }

    public TestUser(Map<String, Object> object) {
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
        Log.e(TAG, "property: "+this.getName()+this.getStream()+this.getSemester());
        Log.e(TAG, "getList(): "+list);
        return list;
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
