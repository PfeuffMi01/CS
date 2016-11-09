package com.example.michael.cs.Data;

import com.example.michael.cs.ListItem;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class Group implements ListItem {

    public String name;
    public int image;


    public Group(String name, int image) {
        this.name = name;
        this.image = image;

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
