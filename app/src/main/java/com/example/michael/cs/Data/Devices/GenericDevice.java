package com.example.michael.cs.Data.Devices;

import android.content.Context;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

/**
 * Created by Patrick PC on 16.11.2016.
 */

public class GenericDevice extends Device {
    public static int numberOfGenericevices;


    public GenericDevice(Context context, String _id, boolean isOn, String name, Room room, Group group, String topic) {
        super(context,_id, isOn, name, room, group, topic);
    }
}
