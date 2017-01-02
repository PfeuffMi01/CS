package com.example.michael.cs.Data.Devices;

import android.content.Context;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

/**
 * Created by Patrick PC on 17.11.2016.
 */

public class HumiditySensor extends Device {

    private int humidity;

    public HumiditySensor(int deviceType, Context context, String _id, boolean isOn, String name, Room room, Group group, int humidity, String topic) {
        super(deviceType, context,_id, isOn, name, room, group, topic);

        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

}
