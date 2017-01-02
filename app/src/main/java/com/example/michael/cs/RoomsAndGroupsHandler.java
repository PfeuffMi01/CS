package com.example.michael.cs;

import android.content.Context;
import android.util.Log;

import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;

import java.util.ArrayList;

import static com.example.michael.cs.Constants.GROUP_DOOR_SENSOR;
import static com.example.michael.cs.Constants.GROUP_HUMIDITY;
import static com.example.michael.cs.Constants.GROUP_LAMPS;
import static com.example.michael.cs.Constants.GROUP_MOVEMENT_SENSOR;
import static com.example.michael.cs.Constants.GROUP_PLUGS;
import static com.example.michael.cs.Constants.GROUP_TEMP;
import static com.example.michael.cs.Constants.GROUP_WINDOW_SENSOR;
import static com.example.michael.cs.Constants.MQTT_TOPIC_BEDROOM;
import static com.example.michael.cs.Constants.MQTT_TOPIC_FLOOR;
import static com.example.michael.cs.Constants.MQTT_TOPIC_GARAGE;
import static com.example.michael.cs.Constants.MQTT_TOPIC_GARDEN;
import static com.example.michael.cs.Constants.MQTT_TOPIC_KITCHEN;
import static com.example.michael.cs.Constants.MQTT_TOPIC_LIVINGROOM;
import static com.example.michael.cs.Constants.MQTT_TOPIC_OFFICE;
import static com.example.michael.cs.Constants.ROOM_BED;
import static com.example.michael.cs.Constants.ROOM_GARAGE;
import static com.example.michael.cs.Constants.ROOM_GARDEN;
import static com.example.michael.cs.Constants.ROOM_HALLWAY;
import static com.example.michael.cs.Constants.ROOM_KITCHEN;
import static com.example.michael.cs.Constants.ROOM_LIVING;
import static com.example.michael.cs.Constants.ROOM_OFFICE;

/**
 * Created by Patrick PC on 22.12.2016.
 */


public class RoomsAndGroupsHandler {

    private static RoomsAndGroupsHandler thisInstance;
    private static final String TAG = "RoomsAndGroupsHandler";
    private Context context;
    private ArrayList<Room> roomList;
    private ArrayList<Group> groupList;

    /**
     * Singleton Initialisierung
     *
     * @param c
     * @return
     */
    public static RoomsAndGroupsHandler getInstance(Context c) {

        if (thisInstance == null) {
            thisInstance = new RoomsAndGroupsHandler(c);
        }

        return thisInstance;
    }

    public RoomsAndGroupsHandler(Context c) {
        this.context = c;
        roomList = new ArrayList<>();
        groupList = new ArrayList<>();

        initData();
    }

    private void initData() {

        Room livingRoom = new Room(R.drawable.living_room, ROOM_LIVING, MQTT_TOPIC_LIVINGROOM);
        Room bedRoom = new Room(R.drawable.bed_room, ROOM_BED, MQTT_TOPIC_BEDROOM);
        Room garageRoom = new Room(R.drawable.garage_room, ROOM_GARAGE, MQTT_TOPIC_GARAGE);
        Room hallwayRoom = new Room(R.drawable.hallway, ROOM_HALLWAY, MQTT_TOPIC_FLOOR);
        Room gardenRoom = new Room(R.drawable.garden_room_, ROOM_GARDEN, MQTT_TOPIC_GARDEN);
        Room kitchenRoom = new Room(R.drawable.kitchen_room, ROOM_KITCHEN, MQTT_TOPIC_KITCHEN);
        Room officeRoom = new Room(R.drawable.office_room, ROOM_OFFICE, MQTT_TOPIC_OFFICE);

        Group plugs = new Group(GROUP_PLUGS, R.drawable.plug_group);
        Group movementSens = new Group(GROUP_MOVEMENT_SENSOR, R.drawable.movement_sens);
        Group doorSens = new Group(GROUP_DOOR_SENSOR, R.drawable.door_sens);
        Group windowSens = new Group(GROUP_WINDOW_SENSOR, R.drawable.window_sens);
        Group humidity = new Group(GROUP_HUMIDITY, R.drawable.humidity);
        Group temperature = new Group(GROUP_TEMP, R.drawable.temp);
        Group lamps = new Group(GROUP_LAMPS, R.drawable.lamps_group);

        roomList.add(livingRoom);
        roomList.add(bedRoom);
        roomList.add(garageRoom);
        roomList.add(hallwayRoom);
        roomList.add(gardenRoom);
        roomList.add(kitchenRoom);
        roomList.add(officeRoom);

        groupList.add(plugs);
        groupList.add(movementSens);
        groupList.add(doorSens);
        groupList.add(windowSens);
        groupList.add(humidity);
        groupList.add(temperature);
        groupList.add(lamps);
    }

    public ArrayList<Room> getRoomList() {
        return roomList;
    }

    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    public Room getRoomByName(String name) {

        for (Room r : roomList) {
            if (r.getName().equals(name)) {
                Log.i(TAG, "getRoomByName: returning NOT null " + name + " -- " + r.getName());
                return r;
            }
        }
        Log.i(TAG, "getRoomByName: returning null " + name);
        return null;
    }

    public Group getGroupByName(String name) {

        for (Group g : groupList) {
            if (g.getName().equals(name)) {
                Log.i(TAG, "getGroupByName: returning NOT null " + name + " -- " + g.getName());
                return g;
            }
        }
        Log.i(TAG, "getGroupByName: returning null " + name);
        return null;
    }
}
