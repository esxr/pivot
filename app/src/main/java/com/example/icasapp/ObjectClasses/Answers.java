package com.example.icasapp.ObjectClasses;

import java.util.Date;

public class Answers extends AnswersPostId{

    private String answer ,user_id;
    public int upvotes;
    public Date timestamp;


    public Answers(){

    }

    public Answers(String answer,int upvotes, String user_id, Date timestamp) {
        this.answer=answer;
        this.upvotes = upvotes;
        this.user_id = user_id;
        this.timestamp = timestamp;
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


}