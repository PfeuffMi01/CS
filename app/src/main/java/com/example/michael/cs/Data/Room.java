package com.example.michael.cs.Data;

import com.example.michael.cs.ListItem;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class Room implements ListItem {

    public int image;
    String name;

    public Room(int image, String name) {
        this.image = image;
        this.name = name;
    }



    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getImage() {
        return image;
    }

    @Override
    public void setName(String s) {
        this.name = s;
    }

    @Override
    public void setImage(int i) {
        this.image = i;
    }
}
