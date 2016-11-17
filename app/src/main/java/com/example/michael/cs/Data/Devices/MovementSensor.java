package com.example.michael.cs.Data.Devices;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

/**
 * Created by Patrick PC on 17.11.2016.
 */

public class MovementSensor extends Device {

    private String lastMovement;

    public MovementSensor(String _id, boolean isOn, String name, Room room, Group group, String lastMovement) {
        super(_id, isOn, name, room, group);

        this.lastMovement = lastMovement;

    }

    public String getLastMovement() {
        return lastMovement;
    }

    public void setLastMovement(String lastMovement) {
        this.lastMovement = lastMovement;
    }
}
