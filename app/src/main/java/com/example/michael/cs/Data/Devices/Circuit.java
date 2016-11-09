package com.example.michael.cs.Data.Devices;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Rooms.Room;

/**
 * Created by Patrick PC on 07.11.2016.
 */

public class Circuit extends Device {
    public Circuit(String _id, boolean isOn, String name, Room room, Group group) {
        super(_id, isOn, name, room, group);
    }
}
