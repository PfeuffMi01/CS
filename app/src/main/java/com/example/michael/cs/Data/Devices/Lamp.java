package com.example.michael.cs.Data.Devices;

import android.content.Context;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class Lamp extends Device {

    public int dim;

    public Lamp(Context context, String _id, boolean isOn, String name, Room room, Group group, int dim, String topic) {
        super(context,_id, isOn, name, room, group, topic);

        this.dim = dim;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }
}
