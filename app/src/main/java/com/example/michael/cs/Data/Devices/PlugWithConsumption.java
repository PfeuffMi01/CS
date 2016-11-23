package com.example.michael.cs.Data.Devices;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

/**
 * Created by Patrick PC on 17.11.2016.
 */

public class PlugWithConsumption extends Plug {
    private String consumption;

    public PlugWithConsumption(String _id, boolean isOn, String name, Room room, Group group, String status, String consumption, String topic) {
        super(_id, isOn, name, room, group, status, topic);

        this.consumption = consumption;
    }


    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }
}
