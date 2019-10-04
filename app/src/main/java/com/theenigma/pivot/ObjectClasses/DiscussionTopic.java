package com.theenigma.pivot.ObjectClasses;

import java.util.Date;

public class DiscussionTopic extends com.theenigma.pivot.ObjectClasses.DiscussionPostid {

    public String user_id, image_url, content, image_thumb;
    public Date timestamp;
    public int question;

    public DiscussionTopic() {
    }

    public DiscussionTopic(String user_id, String image_url, String content, String image_thumb, Date timestamp, int question) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.content = content;
        this.image_thumb = image_thumb;
        this.timestamp = timestamp;
        this.question = question;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getQuestion() { return question; }

    public void setQuestion(int question) { this.question = question; }


}
