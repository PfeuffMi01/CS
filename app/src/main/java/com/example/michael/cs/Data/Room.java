package com.example.michael.cs.Data;

import com.example.michael.cs.List_Stuff.ListItem;

import static com.example.michael.cs.Constants.MQTT_TOPIC_MAIN;

/**
 * Repräsentiert einen Raum, wie z.B. Wohnzimmer oder Küche
 */


public class Room   implements ListItem,Comparable<Room> {

    public int image;
    public String name;
    public String topic;



    public Room(int image, String name, String topic) {
        this.topic = MQTT_TOPIC_MAIN + topic;
        this.image = image;
        this.name = name;
        this.topic = MQTT_TOPIC_MAIN + topic;
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

    @Override
    public int compareTo(Room room) {
        return this.getName().compareTo(room.getName());
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
