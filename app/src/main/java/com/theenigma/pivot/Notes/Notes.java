package com.theenigma.pivot.Notes;

public class Notes {

    private String fileName;
    private String semester;
    private String sessional;
    private String subject;
    private String username;
    private String downloadURL;


    public Notes(){

    }

    public Notes(String fileName, String semester, String sessional, String username, String subject, String downloadURL) {
        this.fileName = fileName;
        this.semester = semester;
        this.sessional = sessional;
        this.username = username;
        this.subject = subject;
        this.downloadURL = downloadURL;
    }

    String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    String getSessional() {
        return sessional;
    }

    public void setSessional(String sessional) {
        this.sessional = sessional;
    }


    String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
