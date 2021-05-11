package com.academia.gorillas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Subcategory implements Serializable {

    private String title;
    private List<Link> links = new ArrayList<>();

    public Subcategory(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<Link> getLinks() {
        return links;
    }
}
