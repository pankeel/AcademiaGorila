package com.academia.gorillas.model;

import java.io.Serializable;

public class Category implements Serializable {

    private int id;
    private int count;
    private String name;

    public Category(int id, int count, String name) {
        this.id = id;
        this.count = count;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}
