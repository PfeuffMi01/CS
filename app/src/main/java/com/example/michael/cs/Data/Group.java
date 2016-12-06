package com.example.michael.cs.Data;

import com.example.michael.cs.List_Stuff.ListItem;

/**
 * Repräsentiert eine Gruppe, wie z.B. Lampe oder Bewegungsmelder
 */

public class Group implements ListItem, Comparable<Group> {

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

    @Override
    public int compareTo(Group group) {
        return this.getName().compareTo(group.getName());
    }
}
