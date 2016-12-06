package com.example.michael.cs.List_Stuff;

/**
 * Interface für ListItems
 * wird in der onBindViewHolder aufgerufen
 */
public interface ListItem {

    static final String TAG = "ListItem";


    /**
     * GETTERS ++++++++++++++++++++++++++++++++++
     */
    String getName();

    int getImage();


    /**
     * SETTERS ++++++++++++++++++++++++++++++++++
     */

    void setName(String s);

    void setImage(int i);
}