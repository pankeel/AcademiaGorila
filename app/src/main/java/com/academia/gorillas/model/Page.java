package com.academia.gorillas.model;

import java.io.Serializable;

public class Page implements Serializable {

    private int id;
    private String title;
    private String excerpt;
    private String content;
    private String featuredMedia;
    private String date;
    private String link;

    public Page(int id, String title, String excerpt, String content, String featuredMedia, String date, String link) {
        this.id = id;
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
        this.featuredMedia = featuredMedia;
        this.date = date;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public String getContent() {
        return content;
    }

    public String getFeaturedMedia() {
        return featuredMedia;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return title;
    }
}
