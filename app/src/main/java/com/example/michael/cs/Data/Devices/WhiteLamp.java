package com.example.michael.cs.Data.Devices;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Rooms.Room;
import com.example.michael.cs.R;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class WhiteLamp extends Lamp {
    public static final int imgRgbLamp = R.drawable.lamp;

    public WhiteLamp(String _id, boolean isOn, String name, Room room, Group group, int dim) {
        super(_id, isOn, name, room, group, dim);
    }
}
