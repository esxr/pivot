package com.example.icasapp.User;

import java.util.HashMap;

public class TestUser {
    String name, semester, stream;

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
