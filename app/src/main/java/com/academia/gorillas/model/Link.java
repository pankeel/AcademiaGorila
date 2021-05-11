package com.academia.gorillas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Link implements Serializable {
    private String name;
    private String link;
    private List<Subcategory> subcategories = new ArrayList<>();

    public Link(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }
}
