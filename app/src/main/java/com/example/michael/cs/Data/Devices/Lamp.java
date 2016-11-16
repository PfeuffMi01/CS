package com.example.michael.cs.Data.Devices;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class Lamp extends Device {

    public int dim;

    public Lamp(String _id, boolean isOn, String name, Room room, Group group, int dim) {
        super(_id, isOn, name, room, group);

        this.dim = dim;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }
}
