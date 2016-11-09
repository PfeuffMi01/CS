package com.example.michael.cs.Data.Devices;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Rooms.Room;
import com.example.michael.cs.R;

/**
 * Created by Patrick PC on 31.10.2016.
 */

public class RGBLamp extends Lamp {

    public static final int imgRgbLamp = R.drawable.lamp_rgb;
    public String colorHex;

    public RGBLamp(String _id, boolean isOn, String name, Room room, Group group, int dim, String colorHex) {
        super(_id, isOn, name, room, group, dim);

        this.colorHex = colorHex;

    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
}
