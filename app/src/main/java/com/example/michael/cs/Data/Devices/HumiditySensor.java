package com.example.michael.cs.Data.Devices;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

/**
 * Created by Patrick PC on 17.11.2016.
 */

public class HumiditySensor extends Device {

    private int humidity;

    public HumiditySensor(String _id, boolean isOn, String name, Room room, Group group, int humidity, String topic) {
        super(_id, isOn, name, room, group, topic);

        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

}
