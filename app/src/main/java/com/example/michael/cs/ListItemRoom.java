package com.example.michael.cs;

/**
 * Interface for ListItems
 *
 * @author Patrick Engelhardt
 */
public interface ListItemRoom {

    static final String TAG = "ListItemRoom";


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