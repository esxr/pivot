package com.example.icasapp.ObjectClasses;

import java.util.Date;

public class Questions extends com.example.icasapp.ObjectClasses.QuestionsId{

    private String topic,content, user_id;
    private Date timestamp;


    public Questions(){

    }

    public Questions(String message,String topic, String user_id, Date timestamp) {
        this.topic=topic;
        this.content = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String message) {
        this.content = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTopic(){ return topic; }

    public void setTopic(String topic){ this.topic=topic; }


}