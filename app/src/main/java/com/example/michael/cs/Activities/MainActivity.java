package com.example.michael.cs.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;

import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Data.Devices.Plug;
import com.example.michael.cs.Data.Devices.RGBLamp;
import com.example.michael.cs.Data.Devices.Temp;
import com.example.michael.cs.Data.Devices.WhiteLamp;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Rooms.BathRoom;
import com.example.michael.cs.Data.Rooms.BedRoom;
import com.example.michael.cs.Data.Rooms.DiningRoom;
import com.example.michael.cs.Data.Rooms.GarageRoom;
import com.example.michael.cs.Data.Rooms.LivingRoom;
import com.example.michael.cs.Data.Rooms.Room;
import com.example.michael.cs.Fragments.AllFragment;
import com.example.michael.cs.Fragments.GroupFragment;
import com.example.michael.cs.Fragments.HueFragment;
import com.example.michael.cs.Fragments.RoomFragment;
import com.example.michael.cs.R;

import java.util.ArrayList;

import static com.example.michael.cs.R.id.fragment_container;
import static com.example.michael.cs.R.string.group_lamps;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    public static final int HUE_FRAGMENT = 0;
    public static final int ALL_FRAGMENT = 1;
    public static final int ROOM_FRAGMENT = 2;
    public static final int GROUP_FRAGMENT = 3;

    public String GROUP_LAMPS, GROUP_TEMP, GROUP_CIRCUITS, GROUP_PLUGS;

    public int STARTPAGE_FRAGMENT = HUE_FRAGMENT;
    public int CURRENT_FRAGMENT;

    private BottomNavigationView bottomNavigationView;

    private HueFragment hueFragment;
    private AllFragment allFragment;
    private RoomFragment roomFragment;
    private GroupFragment groupFragment;

    private ArrayList<Room> roomsList;
    private ArrayList<Group> groupList;

    private ArrayList<Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initStrings();
        initBottomNavigation();

        initExampleData();

        showStartFragment();

    }

    private void initStrings() {

        GROUP_CIRCUITS = getString(R.string.group_circuit);
        GROUP_LAMPS = getString(group_lamps);
        GROUP_PLUGS = getString(R.string.group_plug);
        GROUP_TEMP = getString(R.string.group_temp);

    }

    private void initExampleData() {

        roomsList = new ArrayList<>();
        LivingRoom livingRoom = new LivingRoom(R.drawable.living_room, "Wohnzimmer");
        BedRoom bedRoom = new BedRoom(R.drawable.bed_room, "Schlafzimmer");
        GarageRoom garageRoom = new GarageRoom(R.drawable.garage_room, "Garage");
        BathRoom bathRoom = new BathRoom(R.drawable.bath_room, "Badezimmer");
        DiningRoom diningRoom = new DiningRoom(R.drawable.dining_room, "Esszimmer");

        roomsList.add(livingRoom);
        roomsList.add(bedRoom);
        roomsList.add(garageRoom);
        roomsList.add(bathRoom);
        roomsList.add(diningRoom);

        groupList = new ArrayList<>();
        Group lamps = new Group(GROUP_LAMPS, R.drawable.lamps_group);
        Group temp = new Group(GROUP_TEMP, R.drawable.temp);
        Group circuit = new Group(GROUP_CIRCUITS, R.drawable.circuit_group);
        Group plugs = new Group(GROUP_PLUGS, R.drawable.plug_group);

        groupList.add(lamps);
        groupList.add(temp);
        groupList.add(circuit);
        groupList.add(plugs);

        deviceList = new ArrayList<>();

        deviceList.add(new RGBLamp("rgblamp1", false, "Lampe 1", livingRoom, lamps, 0, "#f5500c"));

        deviceList.add(new WhiteLamp("whitelamp1", false, "Lampe 2", bedRoom, lamps, 0));
        deviceList.add(new WhiteLamp("whitelamp2", true, "Lampe 3", garageRoom, lamps, 0));

        deviceList.add(new Temp("temp1", true, "Temp 1", diningRoom, temp, 24.2));
        deviceList.add(new Temp("temp2", false, "Temp 2", livingRoom, temp, 24.2));
        deviceList.add(new Temp("temp3", true, "Temp 3", bedRoom, temp, 24.2));

        deviceList.add(new Plug("plug1", false, "Steckdose 1", bathRoom, plugs));
        deviceList.add(new Plug("plug2", true, "Steckdose 2", garageRoom, plugs));
        deviceList.add(new Plug("plug3", false, "Steckdose 3", livingRoom, plugs));

//        deviceList.add(new Circuit("circuit", false, "Schalter 1", bathRoom, circuit));

    }

    private void showStartFragment() {

        onNavigationItemSelected(bottomNavigationView.getMenu().getItem(STARTPAGE_FRAGMENT));
        CURRENT_FRAGMENT = STARTPAGE_FRAGMENT;
    }


    /**
     * Initialisiert die drei Tabs am unteren Bildschirmrand um zwischen "Alle" "Räume" und "Gruppen" zu wechseln
     */
    private void initBottomNavigation() {
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (item.getItemId()) {

            case R.id.action_hue:
                hueFragment = new HueFragment();
                fragmentTransaction.replace(fragment_container, hueFragment, "HueFragment");
                fragmentTransaction.commit();

                CURRENT_FRAGMENT = HUE_FRAGMENT;
                break;

            case R.id.action_all:
                allFragment = new AllFragment();
                fragmentTransaction.replace(fragment_container, allFragment, "AllFragment");
                fragmentTransaction.commit();

                CURRENT_FRAGMENT = ALL_FRAGMENT;
                break;
            case R.id.action_rooms:
                roomFragment = new RoomFragment();
                fragmentTransaction.replace(fragment_container, roomFragment, "RoomFragment");
                fragmentTransaction.commit();

                CURRENT_FRAGMENT = ROOM_FRAGMENT;
                break;
            case R.id.action_groups:
                groupFragment = new GroupFragment();
                fragmentTransaction.replace(fragment_container, groupFragment, "GroupFragment");
                fragmentTransaction.commit();

                CURRENT_FRAGMENT = GROUP_FRAGMENT;
                break;
        }
        return false;
    }

    public void callingMainFromRoomClick(int pos, CardView cv) {

    }

    public void callingMainFromGroupClick(String name) {

        if (!name.equals("") && name != null) {
            Log.i(TAG, "callingMainFromGroupClick: " + name);

           /* switch (name) {

                case :

                    break;



            }*/
        }


    }

    public ArrayList<Device> getDeviceList() {
        return deviceList;
    }

    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    public ArrayList<Room> getRoomsList() {
        return roomsList;
    }


}
