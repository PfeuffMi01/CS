package com.example.michael.cs.Data.Devices;


import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

/**
 * Created by Patrick PC on 07.11.2016.
 */

public class Plug extends Device {

    private String status;

    public Plug(String _id, boolean isOn, String name, Room room, Group group, String status) {
        super(_id, isOn, name, room, group);

        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
