package com.example.michael.cs.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Data.Devices.DoorSensor;
import com.example.michael.cs.Data.Devices.HumiditySensor;
import com.example.michael.cs.Data.Devices.MovementSensor;
import com.example.michael.cs.Data.Devices.Plug;
import com.example.michael.cs.Data.Devices.PlugWithConsumption;
import com.example.michael.cs.Data.Devices.RGBLamp;
import com.example.michael.cs.Data.Devices.Temp;
import com.example.michael.cs.Data.Devices.WhiteLamp;
import com.example.michael.cs.Data.Devices.WindowSensor;
import com.example.michael.cs.Data.Group;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.Fragments.AllFragment;
import com.example.michael.cs.Fragments.DeviceSingleSortListFragment;
import com.example.michael.cs.Fragments.GroupFragment;
import com.example.michael.cs.Fragments.RoomFragment;
import com.example.michael.cs.MQTTHandler;
import com.example.michael.cs.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.R.attr.mode;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.michael.cs.Constants.GROUP_DOOR_SENSOR;
import static com.example.michael.cs.Constants.GROUP_HUMIDITY;
import static com.example.michael.cs.Constants.GROUP_LAMPS;
import static com.example.michael.cs.Constants.GROUP_MOVEMENT_SENSOR;
import static com.example.michael.cs.Constants.GROUP_PLUGS;
import static com.example.michael.cs.Constants.GROUP_TEMP;
import static com.example.michael.cs.Constants.GROUP_WINDOW_SENSOR;
import static com.example.michael.cs.Constants.MQTT_TOPIC_BEDROOM;
import static com.example.michael.cs.Constants.MQTT_TOPIC_DOOR;
import static com.example.michael.cs.Constants.MQTT_TOPIC_FLOOR;
import static com.example.michael.cs.Constants.MQTT_TOPIC_GARAGE;
import static com.example.michael.cs.Constants.MQTT_TOPIC_GARDEN;
import static com.example.michael.cs.Constants.MQTT_TOPIC_HUMIDITY;
import static com.example.michael.cs.Constants.MQTT_TOPIC_KITCHEN;
import static com.example.michael.cs.Constants.MQTT_TOPIC_LIGHT;
import static com.example.michael.cs.Constants.MQTT_TOPIC_LIVINGROOM;
import static com.example.michael.cs.Constants.MQTT_TOPIC_MOTION;
import static com.example.michael.cs.Constants.MQTT_TOPIC_OFFICE;
import static com.example.michael.cs.Constants.MQTT_TOPIC_SOCKET;
import static com.example.michael.cs.Constants.MQTT_TOPIC_TEMPERATURE;
import static com.example.michael.cs.Constants.MQTT_TOPIC_WINDOW;
import static com.example.michael.cs.Constants.ROOM_BED;
import static com.example.michael.cs.Constants.ROOM_GARAGE;
import static com.example.michael.cs.Constants.ROOM_GARDEN;
import static com.example.michael.cs.Constants.ROOM_HALLWAY;
import static com.example.michael.cs.Constants.ROOM_KITCHEN;
import static com.example.michael.cs.Constants.ROOM_LIVING;
import static com.example.michael.cs.Constants.ROOM_OFFICE;
import static com.example.michael.cs.Constants.STATUS_OK;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int ALL_FRAGMENT = 0;
    public static final int ROOM_FRAGMENT = 1;
    public static final int GROUP_FRAGMENT = 2;
    public static final int DEVICE_SINGLE_SORT_LIST_FRAGMENT = 3;

    public int CURRENT_FRAGMENT;
    public int CURRENT_LIST_CATEGORY;

    private AllFragment allFragment;
    private RoomFragment roomFragment;
    private GroupFragment groupFragment;

    private FrameLayout fragContainer;

    private ArrayList<Room> roomsList;
    private ArrayList<Group> groupList;

    private ArrayList<Device> deviceList;
    private DeviceSingleSortListFragment deviceSingleSortListFragment;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MQTTHandler mqttHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startService(new Intent(this, MQTTService.class));

        initMQTT();
        initExampleData();
        initFragment();
        initTabs();
    }

    private void initMQTT() {
        mqttHandler = MQTTHandler.getInstance(this);
    }

    public MQTTHandler getMqttHandler() {
        return mqttHandler;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void showMQTTLogDialog() {
        final LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_mqtt_log, null);

        ListView listView = (ListView) dialogView.findViewById(R.id.mqtt_log_list);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String log = sharedPreferences.getString("pref_mqtt_log", "");
        String[] listMqttLog = log.split("\\|", -1);

        if (listMqttLog.length == 1 && listMqttLog[0].equals("")) {
            listMqttLog[0] = "Bisher keine Log-Einträge";
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item, R.id.text_mqtt_log, listMqttLog);
        listView.setAdapter(adapter);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("MQTT Log");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nix machen
                    }
                }

        );

        dialogBuilder.setNeutralButton("Log löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("pref_mqtt_log") ;
                editor.apply();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void initFragment() {

        fragContainer = (FrameLayout) findViewById(R.id.fragment_container);
        fragContainer.setVisibility(GONE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (deviceSingleSortListFragment == null) {
            deviceSingleSortListFragment = new DeviceSingleSortListFragment();
        }
        fragmentTransaction.replace(R.id.fragment_container, deviceSingleSortListFragment, "DeviceSingleSortListFragment");
        fragmentTransaction.commit();

        allFragment = new AllFragment();
        roomFragment = new RoomFragment();
        groupFragment = new GroupFragment();
    }

    private void initTabs() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                Log.i(TAG, "onPageSelected: " + position);
                if (position == ALL_FRAGMENT) {
                    CURRENT_FRAGMENT = ALL_FRAGMENT;
                } else {
                    CURRENT_FRAGMENT = DEVICE_SINGLE_SORT_LIST_FRAGMENT;

                    if (position == ROOM_FRAGMENT) {
                        CURRENT_LIST_CATEGORY = ROOM_FRAGMENT;
                    } else if (position == GROUP_FRAGMENT) {
                        CURRENT_LIST_CATEGORY = GROUP_FRAGMENT;
                    }
                }
            }
        });

        CURRENT_FRAGMENT = ALL_FRAGMENT;
    }

    private void setupTabIcons() {
        TypedArray imgs = getResources().obtainTypedArray(R.array.tab_icons);

        for (int i = 0; i < imgs.length(); i++) {
            tabLayout.getTabAt(i).setIcon(imgs.getResourceId(i, -1));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(allFragment, "Alle");
        adapter.addFragment(roomFragment, "Räume");
        adapter.addFragment(groupFragment, "Gruppen");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final FragmentManager fragmentManager;
        private Fragment fragmentAtPos0;
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            fragmentManager = manager;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.i(TAG, "getPageTitle: " + fragmentTitleList.get(position));
            return fragmentTitleList.get(position);
        }
    }

    private void initExampleData() {

        roomsList = new ArrayList<>();
        groupList = new ArrayList<>();
        deviceList = new ArrayList<>();

        Room livingRoom = new Room(R.drawable.living_room, ROOM_LIVING, MQTT_TOPIC_LIVINGROOM);
        Room bedRoom = new Room(R.drawable.bed_room, ROOM_BED, MQTT_TOPIC_BEDROOM);
        Room garageRoom = new Room(R.drawable.garage_room, ROOM_GARAGE, MQTT_TOPIC_GARAGE);
        Room hallwayRoom = new Room(R.drawable.hallway, ROOM_HALLWAY, MQTT_TOPIC_FLOOR);
        Room gardenRoom = new Room(R.drawable.garden_room_, ROOM_GARDEN, MQTT_TOPIC_GARDEN);
        Room kitchenRoom = new Room(R.drawable.kitchen_room, ROOM_KITCHEN, MQTT_TOPIC_KITCHEN);
        Room officeRoom = new Room(R.drawable.office_roomm, ROOM_OFFICE, MQTT_TOPIC_OFFICE);


        roomsList.add(livingRoom);
        roomsList.add(bedRoom);
        roomsList.add(garageRoom);
        roomsList.add(hallwayRoom);
        roomsList.add(gardenRoom);
        roomsList.add(kitchenRoom);
        roomsList.add(officeRoom);
        Collections.sort(roomsList);

        Group lamps = new Group(GROUP_LAMPS, R.drawable.lamps_group);
        Group plugs = new Group(GROUP_PLUGS, R.drawable.plug_group);
        Group movementSens = new Group(GROUP_MOVEMENT_SENSOR, R.drawable.movement_sens);
        Group doorSens = new Group(GROUP_DOOR_SENSOR, R.drawable.door_sens);
        Group windowSens = new Group(GROUP_WINDOW_SENSOR, R.drawable.window_sens);
        Group humidity = new Group(GROUP_HUMIDITY, R.drawable.humidity);
        Group temperature = new Group(GROUP_TEMP, R.drawable.temp);

        groupList.add(lamps);
        groupList.add(plugs);
        groupList.add(movementSens);
        groupList.add(doorSens);
        groupList.add(windowSens);
        groupList.add(humidity);
        groupList.add(temperature);
        Collections.sort(groupList);

        deviceList.add(new RGBLamp(this, "rgblamp1", false, "Philips LED RGB Lampe", livingRoom, lamps, 0, getResources().getColor(R.color.white), STATUS_OK, MQTT_TOPIC_LIGHT));
        deviceList.add(new WhiteLamp(this, "whitelamp1", false, "Philips LED weiss ", officeRoom, lamps, 12, STATUS_OK, MQTT_TOPIC_LIGHT));
        deviceList.add(new RGBLamp(this, "rgblamp2", false, "Osram LED RGB Lampe ", kitchenRoom, lamps, 50, getResources().getColor(R.color.white), STATUS_OK, MQTT_TOPIC_LIGHT));

        deviceList.add(new Plug(this, "plug", false, "Homematic Steckdose", bedRoom, plugs, STATUS_OK, MQTT_TOPIC_SOCKET));

        deviceList.add(new MovementSensor(this, "move1", true, "Philips Bewegungssensor", hallwayRoom, movementSens, "17.11.16 12:45 Uhr", MQTT_TOPIC_MOTION));

        deviceList.add(new DoorSensor(this, "door1", true, "Türsensor ", hallwayRoom, doorSens, STATUS_OK, MQTT_TOPIC_DOOR));
        deviceList.add(new WindowSensor(this, "window1", true, "Fenstersensor ", livingRoom, windowSens, STATUS_OK, MQTT_TOPIC_WINDOW));

        deviceList.add(new MovementSensor(this, "move2", true, "Homematic Bewegungssensor", garageRoom, movementSens, "16.11.16 13:32 Uhr", MQTT_TOPIC_MOTION));

        deviceList.add(new Temp(this, "temp", true, "Homematic Wetterstation", gardenRoom, temperature, 5, MQTT_TOPIC_TEMPERATURE));
        deviceList.add(new HumiditySensor(this, "humidity", true, "Homematic Wetterstation", gardenRoom, humidity, 75, MQTT_TOPIC_HUMIDITY));

        deviceList.add(new PlugWithConsumption(this, "plug2", false, "Elgato Steckdose", bedRoom, plugs, STATUS_OK, "5000", MQTT_TOPIC_SOCKET));

        deviceList.add(new RGBLamp(this, "ledBand", false, "LED-Band RGB", bedRoom, lamps, 12, getResources().getColor(R.color.white), STATUS_OK, MQTT_TOPIC_LIGHT));

        for (Device device : deviceList) {
            Log.i(TAG, "initExampleData: " + device.getName() + " " + device.getTopic());
        }
    }


    @Override
    public void onBackPressed() {

        if (CURRENT_FRAGMENT == DEVICE_SINGLE_SORT_LIST_FRAGMENT) {

            fragChanger(GONE, VISIBLE, false, getResources().getString(R.string.app_name), mode);
            CURRENT_FRAGMENT = 999;
        } else {
            super.onBackPressed();
        }
    }

    public void callingMainFromGridClick(String name, int mode) {

        Log.i(TAG, "handleRoomClick: " + name);

        fragChanger(VISIBLE, GONE, true, name, mode);
    }

    private void fragChanger(int visibilityFragContainer, int visibilityViewPager, boolean visibilityToolbarBackArrow, String name, int mode) {

        fragContainer.setVisibility(visibilityFragContainer);
        viewPager.setVisibility(visibilityViewPager);
        tabLayout.setVisibility(visibilityViewPager);
        toolbarManager(name, visibilityToolbarBackArrow);

        if (visibilityFragContainer == VISIBLE) {
            deviceSingleSortListFragment.setSortToShow(name);
        }

        CURRENT_FRAGMENT = DEVICE_SINGLE_SORT_LIST_FRAGMENT;


    }

    public void toolbarManager(String title, boolean show) {

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        getSupportActionBar().setDisplayShowHomeEnabled(show);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_log:
                showMQTTLogDialog();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
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

    public int getCURRENT_LIST_CATEGORY() {
        return CURRENT_LIST_CATEGORY;
    }

    public void setCURRENT_LIST_CATEGORY(int CURRENT_LIST_CATEGORY) {
        this.CURRENT_LIST_CATEGORY = CURRENT_LIST_CATEGORY;
    }


    public void listItemHasBeenClicked(int adapterPosition, int listItemType, View view) {
        Log.i(TAG, "listItemHasBeenClicked: Pos: " + adapterPosition + " Cur. Frag.: " + CURRENT_FRAGMENT);

        if (CURRENT_FRAGMENT == ALL_FRAGMENT) {
            Log.i(TAG, "listItemHasBeenClicked: ALL_FRAGMENT");
            allFragment.openDialog(adapterPosition, listItemType);
        } else {
            deviceSingleSortListFragment.openDialog(adapterPosition, listItemType);
        }
    }

    public void switchInItemHasBeenClicked(int adapterPosition, boolean b) {

        Log.i(TAG, "switchInItemHasBeenClicked: " + adapterPosition + " " + b);

        if (CURRENT_FRAGMENT == ALL_FRAGMENT) {
            allFragment.switchTheSwitch(adapterPosition, b);
        } else {
            deviceSingleSortListFragment.switchTheSwitch(adapterPosition, b);
        }
    }

     /*   if (CURRENT_FRAGMENT == ALL_FRAGMENT) {

            deviceList.get(adapterPosition).setOn(!deviceList.get(adapterPosition).isOn());
            listener.onDataHasChanged(adapterPosition);

            allFragment.changeSwitchState(adapterPosition, b);
        } else if (CURRENT_FRAGMENT == DEVICE_SINGLE_SORT_LIST_FRAGMENT) {
            deviceSingleSortListFragment.changeSwitchState(adapterPosition, b);
        }*/

}
