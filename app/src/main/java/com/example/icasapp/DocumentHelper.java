package com.example.icasapp;

public class DocumentHelper {
//FOR INTEGRATION WITH THE NOTES FRAGMENT AND THE INTENDED ACTIVITIES
    public String name;
    public String url;

    public DocumentHelper(){

    }

    public DocumentHelper(String name, String url){
        this.name = name;
        this.url = url;
    }

    public String getName(){
        return name;
    }

    public String getUrl(){
        return url;
    }
}
