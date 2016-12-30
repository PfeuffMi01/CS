package com.example.michael.cs.Data.Devices;


import android.content.Context;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.Interfaces.OnDataChangedListener;

/**
 * Vaterklasse eines jeden Geräts, wie z.B. "Lampe RGB"
 */


public class Device implements Cloneable {

    public String _id;
    public boolean isOn;
    public String name;
    public Room room;
    public Group group;
    public String topic;
    public Context context;

    public Device(Context context, String _id, boolean isOn, String name, Room room, Group group, String topic) {
        this.context = context;
        this._id = _id;
        this.isOn = isOn;
        this.name = name;
        this.room = room;
        this.group = group;
        this.topic = this.room.getTopic() + topic;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void showDialogForThisDevice(final MainActivity mainActivity, OnDataChangedListener dataChangedListener, int adapterPosition) {

    }
}
