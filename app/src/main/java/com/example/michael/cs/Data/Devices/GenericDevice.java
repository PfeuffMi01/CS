package com.example.michael.cs.Data.Devices;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

/**
 * Created by Patrick PC on 16.11.2016.
 */

public class GenericDevice extends Device {
    public static int numberOfGenericevices;


    public GenericDevice(String _id, boolean isOn, String name, Room room, Group group) {
        super(_id, isOn, name, room, group);
    }
}
