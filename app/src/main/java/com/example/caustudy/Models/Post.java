package com.example.caustudy.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

// jesnk

public class Post {

    public String uid;
    public String author;
    public String title;
    public String contents;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String title, String contents) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.contents = contents;
    }


    public String getTitle() {
        return title;
    }
    public String getContents() {
        return contents;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", contents);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
    // [END post_to_map]

}
