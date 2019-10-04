package com.theenigma.pivot.ObjectClasses;

import java.util.Date;

public class Questions extends com.theenigma.pivot.ObjectClasses.QuestionsId{

    private String topic,content, user_id, best_answer, name, image_url;
    private Date timestamp;


    public Questions(){

    }

    public Questions(String message, String topic, String user_id, Date timestamp, String best_answer, String name, String image_url) {
        this.topic=topic;
        this.content = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.best_answer = best_answer;
        this.name = name;
        this.image_url = image_url;
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

    public String getBest_answer(){ return best_answer;}

    public void setBest_answer(String best_answer){ this.best_answer=best_answer; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}