package com.example.michael.cs.Data.Devices;

import android.content.Context;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

/**
 * Created by Patrick PC on 17.11.2016.
 */

public class WindowSensor extends Device {
    private String status;

    public WindowSensor(Context context, String _id, boolean isOn, String name, Room room, Group group, String status, String topic) {
        super(context,_id, isOn, name, room, group, topic);

        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
