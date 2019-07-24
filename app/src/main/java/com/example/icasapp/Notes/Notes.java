package com.example.icasapp.Notes;

public class Notes {
    private String name;
    private String semester;
    private String sessional;

    public Notes() {

    }

    public Notes(String name, String semester, String sessional) {
        this.name = name;
        this.semester = semester;
        this.sessional = sessional;
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

    public String getSessional() {
        return sessional;
    }

    public void setSessional(String sessional) {
        this.sessional = sessional;
    }
}


