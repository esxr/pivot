package com.theenigma.pivot.ObjectClasses;

import java.util.Date;

public class Answers extends AnswersPostId{

    private String answer ,user_id, name, image_url;
    private int upvotes;
    private Date timestamp;


    public Answers(){

    }

    public Answers(String answer, int upvotes, String user_id, Date timestamp, String name, String image_url) {
        this.answer=answer;
        this.upvotes = upvotes;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.name = name;
        this.image_url = image_url;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public int getUpvotes(){ return upvotes; }

    public void setUpvotes(int upvotes){ this.upvotes=upvotes; }


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