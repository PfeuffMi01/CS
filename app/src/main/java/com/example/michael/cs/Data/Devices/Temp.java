package com.example.michael.cs.Data.Devices;

import android.content.Context;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.R;

public class Temp extends Device {

    public static final int imgRgbLamp = R.drawable.temp;
    private int temp;

    public Temp(int deviceType, Context context, String _id, boolean isOn, String name, Room room, Group group, int temp, String topic) {
        super(deviceType, context,_id, isOn, name, room, group, topic);

        this.temp = temp;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

}
