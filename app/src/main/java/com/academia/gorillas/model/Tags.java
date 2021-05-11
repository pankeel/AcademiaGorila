package com.academia.gorillas.model;

import java.io.Serializable;
import java.util.List;

public class Tags implements Serializable {

    private List<Tag> tags;

    public Tags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
