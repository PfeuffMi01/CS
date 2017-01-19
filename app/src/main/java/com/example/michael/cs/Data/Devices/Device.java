package com.example.michael.cs.Data.Devices;


import android.content.Context;
import android.util.Log;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.Interfaces.OnDataChangedListener;

/**
 * Vaterklasse eines jeden Geräts, wie z.B. "Lampe RGB"
 */


public class Device implements Cloneable {
    private static final String TAG = "Device";

    public Context context;

    private Room room;
    private Group group;

    private String _id;
    private String name;
    private String topic;

    private boolean isOn;

    private int deviceType;

    public Device(int deviceType, Context context, String _id, boolean isOn, String name, Room room, Group group, String topic) {
        this.deviceType = deviceType;
        this.context = context;
        this._id = _id;
        this.isOn = isOn;
        this.name = name;
        this.room = room;
        this.group = group;
        this.topic = topic;

        Log.i(TAG, "Device: " + name + " " + room.getName() + " " + group.getName());
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

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
