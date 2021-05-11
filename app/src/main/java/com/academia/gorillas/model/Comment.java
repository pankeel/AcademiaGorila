package com.academia.gorillas.model;

public class Comment {

    private int id;
    private String authorName;
    private String content;
    private String date;
    private String avatarUrl;

    public Comment(int id, String authorName, String content, String date, String avatarUrl) {
        this.id = id;
        this.authorName = authorName;
        this.content = content;
        this.date = date;
        this.avatarUrl = avatarUrl;
    }

    public int getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
