package com.example.michael.cs.Data.Devices;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Rooms.Room;
import com.example.michael.cs.R;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class Temp extends Device {

    public static final int imgRgbLamp = R.drawable.temp;
    public double temp;

    public Temp(String _id, boolean isOn, String name, Room room, Group group, double temp) {
        super(_id, isOn, name, room, group);

        this.temp = temp;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
